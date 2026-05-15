package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.LoginCredentials
import amin.codelabs.realestateinvestmentintelligence.domain.model.RegistrationData
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthResult
import amin.codelabs.realestateinvestmentintelligence.domain.service.AuthValidationError
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Immutable
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
)

sealed interface LoginUiEvent {
    data class EmailChanged(val value: String) : LoginUiEvent
    data class PasswordChanged(val value: String) : LoginUiEvent
    data object SubmitClicked : LoginUiEvent
    data object RegisterClicked : LoginUiEvent
}

sealed interface LoginUiEffect {
    data object NavigateToMain : LoginUiEffect
    data object NavigateToRegister : LoginUiEffect
}

interface LoginViewModel {
    val state: LoginUiState
    val effect: LoginUiEffect?

    fun onEvent(event: LoginUiEvent)

    fun consumeEffect()
}

class DefaultLoginViewModel(
    private val authRepository: AuthRepository,
) : LoginViewModel {
    override var state by mutableStateOf(LoginUiState())
        private set

    override var effect by mutableStateOf<LoginUiEffect?>(null)
        private set

    override fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> state = state.copy(
                email = event.value,
                emailError = null,
                generalError = null,
            )

            is LoginUiEvent.PasswordChanged -> state = state.copy(
                password = event.value,
                passwordError = null,
                generalError = null,
            )

            LoginUiEvent.SubmitClicked -> submit()
            LoginUiEvent.RegisterClicked -> effect = LoginUiEffect.NavigateToRegister
        }
    }

    override fun consumeEffect() {
        effect = null
    }

    private fun submit() {
        if (state.isLoading) return

        val submittedEmail = state.email.trim()
        val submittedPassword = state.password
        state = state.copy(
            isLoading = true,
            emailError = null,
            passwordError = null,
            generalError = null,
        )

        when (val result = authRepository.login(
            LoginCredentials(
                email = submittedEmail,
                password = submittedPassword,
            ),
        )) {
            is AuthResult.Success -> {
                state = state.copy(
                    email = "",
                    password = "",
                    isLoading = false,
                )
                effect = LoginUiEffect.NavigateToMain
            }

            is AuthResult.Failure -> {
                state = when (val error = result.error) {
                    is AuthError.ValidationFailed -> state.copy(
                        isLoading = false,
                        emailError = error.errors.toEmailError(),
                        passwordError = error.errors.toPasswordError(),
                    )

                    AuthError.InvalidCredentials -> state.copy(
                        isLoading = false,
                        password = "",
                        generalError = LOGIN_FAILED_MESSAGE,
                    )

                    AuthError.EmailAlreadyRegistered -> state.copy(
                        isLoading = false,
                        generalError = LOGIN_FAILED_MESSAGE,
                    )
                }
            }
        }
    }
}

@Immutable
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
)

sealed interface RegisterUiEvent {
    data class FullNameChanged(val value: String) : RegisterUiEvent
    data class EmailChanged(val value: String) : RegisterUiEvent
    data class PasswordChanged(val value: String) : RegisterUiEvent
    data class ConfirmPasswordChanged(val value: String) : RegisterUiEvent
    data object SubmitClicked : RegisterUiEvent
    data object LoginClicked : RegisterUiEvent
}

sealed interface RegisterUiEffect {
    data object NavigateToMain : RegisterUiEffect
    data object NavigateToLogin : RegisterUiEffect
}

interface RegisterViewModel {
    val state: RegisterUiState
    val effect: RegisterUiEffect?

    fun onEvent(event: RegisterUiEvent)

    fun consumeEffect()
}

class DefaultRegisterViewModel(
    private val authRepository: AuthRepository,
) : RegisterViewModel {
    override var state by mutableStateOf(RegisterUiState())
        private set

    override var effect by mutableStateOf<RegisterUiEffect?>(null)
        private set

    override fun onEvent(event: RegisterUiEvent) {
        when (event) {
            is RegisterUiEvent.FullNameChanged -> state = state.copy(
                fullName = event.value,
                fullNameError = null,
                generalError = null,
            )

            is RegisterUiEvent.EmailChanged -> state = state.copy(
                email = event.value,
                emailError = null,
                generalError = null,
            )

            is RegisterUiEvent.PasswordChanged -> state = state.copy(
                password = event.value,
                passwordError = null,
                confirmPasswordError = null,
                generalError = null,
            )

            is RegisterUiEvent.ConfirmPasswordChanged -> state = state.copy(
                confirmPassword = event.value,
                confirmPasswordError = null,
                generalError = null,
            )

            RegisterUiEvent.SubmitClicked -> submit()
            RegisterUiEvent.LoginClicked -> effect = RegisterUiEffect.NavigateToLogin
        }
    }

    override fun consumeEffect() {
        effect = null
    }

    private fun submit() {
        if (state.isLoading) return

        val submittedData = RegistrationData(
            fullName = state.fullName,
            email = state.email.trim(),
            password = state.password,
            confirmPassword = state.confirmPassword,
        )
        state = state.copy(
            isLoading = true,
            fullNameError = null,
            emailError = null,
            passwordError = null,
            confirmPasswordError = null,
            generalError = null,
        )

        when (val result = authRepository.register(submittedData)) {
            is AuthResult.Success -> {
                state = state.copy(
                    fullName = "",
                    email = "",
                    password = "",
                    confirmPassword = "",
                    isLoading = false,
                )
                effect = RegisterUiEffect.NavigateToMain
            }

            is AuthResult.Failure -> {
                state = when (val error = result.error) {
                    is AuthError.ValidationFailed -> state.copy(
                        isLoading = false,
                        fullNameError = error.errors.toFullNameError(),
                        emailError = error.errors.toEmailError(),
                        passwordError = error.errors.toPasswordError(),
                        confirmPasswordError = error.errors.toConfirmPasswordError(),
                    )

                    AuthError.EmailAlreadyRegistered -> state.copy(
                        isLoading = false,
                        emailError = EMAIL_ALREADY_REGISTERED_MESSAGE,
                    )

                    AuthError.InvalidCredentials -> state.copy(
                        isLoading = false,
                        generalError = REGISTRATION_FAILED_MESSAGE,
                    )
                }
            }
        }
    }
}

private const val LOGIN_FAILED_MESSAGE = "Invalid email or password."
private const val REGISTRATION_FAILED_MESSAGE = "We could not create this demo account."
private const val EMAIL_ALREADY_REGISTERED_MESSAGE = "This email is already registered."

private fun List<AuthValidationError>.toFullNameError(): String? = when {
    contains(AuthValidationError.RequiredName) -> NAME_REQUIRED_MESSAGE
    else -> null
}

private fun List<AuthValidationError>.toEmailError(): String? = when {
    contains(AuthValidationError.InvalidEmail) -> EMAIL_INVALID_MESSAGE
    else -> null
}

private fun List<AuthValidationError>.toPasswordError(): String? = when {
    contains(AuthValidationError.RequiredPassword) -> PASSWORD_REQUIRED_MESSAGE
    contains(AuthValidationError.PasswordTooShort) -> PASSWORD_TOO_SHORT_MESSAGE
    else -> null
}

private fun List<AuthValidationError>.toConfirmPasswordError(): String? = when {
    contains(AuthValidationError.PasswordConfirmationMismatch) -> PASSWORD_CONFIRMATION_MESSAGE
    else -> null
}

private const val NAME_REQUIRED_MESSAGE = "Enter your full name."
private const val EMAIL_INVALID_MESSAGE = "Enter a valid email address."
private const val PASSWORD_REQUIRED_MESSAGE = "Enter a password."
private const val PASSWORD_TOO_SHORT_MESSAGE = "Use at least 8 characters."
private const val PASSWORD_CONFIRMATION_MESSAGE = "Passwords do not match."
