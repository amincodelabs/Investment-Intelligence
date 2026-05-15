package amin.codelabs.realestateinvestmentintelligence.data.repository

import amin.codelabs.realestateinvestmentintelligence.data.auth.AuthEventLogger
import amin.codelabs.realestateinvestmentintelligence.data.auth.AuthLogEvent
import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.LoginCredentials
import amin.codelabs.realestateinvestmentintelligence.domain.model.RegistrationData
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthResult
import amin.codelabs.realestateinvestmentintelligence.domain.service.AuthValidationError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DemoAuthRepositoryTest {
    @Test
    fun `register creates authenticated session for valid registration`() {
        val repository = DemoAuthRepository()

        val session = repository.register(validRegistration()).requireSuccess()

        assertEquals(AuthStatus.Authenticated, session.status)
        assertEquals("Demo Investor", session.user?.fullName)
        assertEquals("investor@example.com", session.user?.email?.normalized)
    }

    @Test
    fun `register returns validation error for invalid email`() {
        val repository = DemoAuthRepository()

        val error = repository.register(
            validRegistration(email = "invalid-email"),
        ).requireFailure()

        assertEquals(
            AuthError.ValidationFailed(listOf(AuthValidationError.InvalidEmail)),
            error,
        )
    }

    @Test
    fun `register returns validation error for empty name`() {
        val repository = DemoAuthRepository()

        val error = repository.register(
            validRegistration(fullName = ""),
        ).requireFailure()

        assertEquals(
            AuthError.ValidationFailed(listOf(AuthValidationError.RequiredName)),
            error,
        )
    }

    @Test
    fun `register returns validation error for short password`() {
        val repository = DemoAuthRepository()

        val error = repository.register(
            validRegistration(
                password = "short",
                confirmPassword = "short",
            ),
        ).requireFailure()

        assertEquals(
            AuthError.ValidationFailed(listOf(AuthValidationError.PasswordTooShort)),
            error,
        )
    }

    @Test
    fun `register returns validation error for password confirmation mismatch`() {
        val repository = DemoAuthRepository()

        val error = repository.register(
            validRegistration(
                password = "password123",
                confirmPassword = "different123",
            ),
        ).requireFailure()

        assertEquals(
            AuthError.ValidationFailed(listOf(AuthValidationError.PasswordConfirmationMismatch)),
            error,
        )
    }

    @Test
    fun `register prevents duplicate email`() {
        val repository = DemoAuthRepository()
        repository.register(validRegistration()).requireSuccess()

        val error = repository.register(
            validRegistration(fullName = "Second Investor"),
        ).requireFailure()

        assertEquals(AuthError.EmailAlreadyRegistered, error)
    }

    @Test
    fun `login returns authenticated session for valid credentials`() {
        val repository = DemoAuthRepository()
        repository.register(validRegistration()).requireSuccess()
        repository.logout()

        val session = repository.login(
            LoginCredentials(
                email = "investor@example.com",
                password = "password123",
            ),
        ).requireSuccess()

        assertEquals(AuthStatus.Authenticated, session.status)
        assertEquals("investor@example.com", session.user?.email?.normalized)
    }

    @Test
    fun `login returns invalid credentials for unknown email`() {
        val repository = DemoAuthRepository()

        val error = repository.login(
            LoginCredentials(
                email = "missing@example.com",
                password = "password123",
            ),
        ).requireFailure()

        assertEquals(AuthError.InvalidCredentials, error)
    }

    @Test
    fun `login returns invalid credentials for wrong password`() {
        val repository = DemoAuthRepository()
        repository.register(validRegistration()).requireSuccess()
        repository.logout()

        val error = repository.login(
            LoginCredentials(
                email = "investor@example.com",
                password = "wrong-password",
            ),
        ).requireFailure()

        assertEquals(AuthError.InvalidCredentials, error)
    }

    @Test
    fun `logout clears current session`() {
        val repository = DemoAuthRepository()
        repository.register(validRegistration()).requireSuccess()

        repository.logout().requireSuccess()

        assertEquals(AuthStatus.SignedOut, repository.getCurrentSession().status)
        assertEquals(null, repository.getCurrentSession().user)
    }

    @Test
    fun `getCurrentSession returns signed out session initially`() {
        val repository = DemoAuthRepository()

        val session = repository.getCurrentSession()

        assertEquals(AuthStatus.SignedOut, session.status)
        assertEquals(null, session.user)
        assertEquals(AuthStatus.SignedOut, repository.getAuthStatus())
    }

    @Test
    fun `auth logger records safe events without sensitive values`() {
        val logger = RecordingAuthEventLogger()
        val repository = DemoAuthRepository(logger = logger)
        val password = "password123"
        val email = "investor@example.com"

        repository.register(
            validRegistration(
                email = email,
                password = password,
                confirmPassword = password,
            ),
        ).requireSuccess()

        val loggedText = logger.events.joinToString(separator = " ") { it.name }
        assertTrue(AuthLogEvent.RegistrationSucceeded in logger.events)
        assertFalse(loggedText.contains(password))
        assertFalse(loggedText.contains(email))
    }

    private fun validRegistration(
        fullName: String = "Demo Investor",
        email: String = "investor@example.com",
        password: String = "password123",
        confirmPassword: String = password,
    ) = RegistrationData(
        fullName = fullName,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
    )

    private fun <T> AuthResult<T>.requireSuccess(): T =
        (this as AuthResult.Success).value

    private fun AuthResult<*>.requireFailure(): AuthError =
        (this as AuthResult.Failure).error

    private class RecordingAuthEventLogger : AuthEventLogger {
        val events = mutableListOf<AuthLogEvent>()

        override fun log(event: AuthLogEvent) {
            events += event
        }
    }
}
