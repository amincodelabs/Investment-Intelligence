package amin.codelabs.realestateinvestmentintelligence.data.auth

interface AuthEventLogger {
    fun log(event: AuthLogEvent)
}

enum class AuthLogEvent {
    RegistrationValidationFailed,
    RegistrationSucceeded,
    RegistrationRejectedDuplicateEmail,
    LoginValidationFailed,
    LoginSucceeded,
    LoginRejectedInvalidCredentials,
    LogoutSucceeded,
}

object NoOpAuthEventLogger : AuthEventLogger {
    override fun log(event: AuthLogEvent) = Unit
}
