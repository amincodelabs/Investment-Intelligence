package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthSession
import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.LoginCredentials
import amin.codelabs.realestateinvestmentintelligence.domain.model.RegistrationData
import amin.codelabs.realestateinvestmentintelligence.domain.model.User
import amin.codelabs.realestateinvestmentintelligence.domain.model.UserId
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthResult
import amin.codelabs.realestateinvestmentintelligence.domain.service.AuthValidationError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class AuthViewModelsTest {
    @Test
    fun `login view model starts with empty state`() {
        val viewModel = DefaultLoginViewModel(FakeAuthRepository())

        assertEquals(LoginUiState(), viewModel.state)
        assertNull(viewModel.effect)
    }

    @Test
    fun `login success emits navigate effect`() {
        val repository = FakeAuthRepository(
            loginResult = AuthResult.Success(authenticatedSession()),
        )
        val viewModel = DefaultLoginViewModel(repository)

        viewModel.onEvent(LoginUiEvent.EmailChanged("investor@example.com"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("password123"))
        viewModel.onEvent(LoginUiEvent.SubmitClicked)

        assertEquals("investor@example.com", repository.lastLoginCredentials?.email)
        assertEquals(LoginUiEffect.NavigateToMain, viewModel.effect)
        assertEquals("", viewModel.state.password)
        assertFalse(viewModel.state.isLoading)
    }

    @Test
    fun `login failure surfaces error`() {
        val repository = FakeAuthRepository(
            loginResult = AuthResult.Failure(AuthError.InvalidCredentials),
        )
        val viewModel = DefaultLoginViewModel(repository)

        viewModel.onEvent(LoginUiEvent.EmailChanged("investor@example.com"))
        viewModel.onEvent(LoginUiEvent.PasswordChanged("wrong-password"))
        viewModel.onEvent(LoginUiEvent.SubmitClicked)

        assertEquals("Invalid email or password.", viewModel.state.generalError)
        assertEquals("", viewModel.state.password)
        assertNull(viewModel.effect)
    }

    @Test
    fun `register validation error maps field errors`() {
        val repository = FakeAuthRepository(
            registerResult = AuthResult.Failure(
                AuthError.ValidationFailed(
                    listOf(
                        AuthValidationError.RequiredName,
                        AuthValidationError.InvalidEmail,
                        AuthValidationError.RequiredPassword,
                        AuthValidationError.PasswordConfirmationMismatch,
                    ),
                ),
            ),
        )
        val viewModel = DefaultRegisterViewModel(repository)

        viewModel.onEvent(RegisterUiEvent.SubmitClicked)

        assertEquals("Enter your full name.", viewModel.state.fullNameError)
        assertEquals("Enter a valid email address.", viewModel.state.emailError)
        assertEquals("Enter a password.", viewModel.state.passwordError)
        assertEquals("Passwords do not match.", viewModel.state.confirmPasswordError)
        assertNull(viewModel.effect)
    }

    @Test
    fun `register success emits navigate effect`() {
        val repository = FakeAuthRepository(
            registerResult = AuthResult.Success(authenticatedSession()),
        )
        val viewModel = DefaultRegisterViewModel(repository)

        viewModel.onEvent(RegisterUiEvent.FullNameChanged("Amina Ali"))
        viewModel.onEvent(RegisterUiEvent.EmailChanged("amina@example.com"))
        viewModel.onEvent(RegisterUiEvent.PasswordChanged("password123"))
        viewModel.onEvent(RegisterUiEvent.ConfirmPasswordChanged("password123"))
        viewModel.onEvent(RegisterUiEvent.SubmitClicked)

        val submittedData = repository.lastRegistrationData
        assertEquals("Amina Ali", submittedData?.fullName)
        assertEquals("amina@example.com", submittedData?.email)
        assertEquals(RegisterUiEffect.NavigateToMain, viewModel.effect)
        assertEquals("", viewModel.state.password)
        assertEquals("", viewModel.state.confirmPassword)
        assertFalse(viewModel.state.isLoading)
    }

    @Test
    fun `login navigation effect is emitted when register link is clicked`() {
        val viewModel = DefaultLoginViewModel(FakeAuthRepository())

        viewModel.onEvent(LoginUiEvent.RegisterClicked)

        assertEquals(LoginUiEffect.NavigateToRegister, viewModel.effect)
    }

    private fun authenticatedSession(): AuthSession = AuthSession(
        user = User(
            id = UserId("demo-user-1"),
            fullName = "Amina Ali",
            email = amin.codelabs.realestateinvestmentintelligence.domain.model.Email("amina@example.com"),
        ),
        status = AuthStatus.Authenticated,
    )

    private class FakeAuthRepository(
        var loginResult: AuthResult<AuthSession> = AuthResult.Failure(AuthError.InvalidCredentials),
        var registerResult: AuthResult<AuthSession> = AuthResult.Failure(AuthError.InvalidCredentials),
    ) : AuthRepository {
        var lastLoginCredentials: LoginCredentials? = null
            private set
        var lastRegistrationData: RegistrationData? = null
            private set
        private var currentSession: AuthSession = AuthSession.SignedOut

        override fun register(data: RegistrationData): AuthResult<AuthSession> {
            lastRegistrationData = data
            return when (val result = registerResult) {
                is AuthResult.Success -> {
                    currentSession = result.value
                    result
                }

                is AuthResult.Failure -> result
            }
        }

        override fun login(credentials: LoginCredentials): AuthResult<AuthSession> {
            lastLoginCredentials = credentials
            return when (val result = loginResult) {
                is AuthResult.Success -> {
                    currentSession = result.value
                    result
                }

                is AuthResult.Failure -> result
            }
        }

        override fun logout(): AuthResult<Unit> {
            currentSession = AuthSession.SignedOut
            return AuthResult.Success(Unit)
        }

        override fun getCurrentSession(): AuthSession = currentSession

        override fun getAuthStatus(): AuthStatus = currentSession.status
    }
}
