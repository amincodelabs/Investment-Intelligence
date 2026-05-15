package amin.codelabs.realestateinvestmentintelligence.domain.model

data class UserId(
    val value: String,
)

data class Email(
    val value: String,
) {
    val normalized: String = value.trim().lowercase()
}

data class User(
    val id: UserId,
    val fullName: String,
    val email: Email,
)

data class AuthSession(
    val user: User?,
    val status: AuthStatus,
) {
    companion object {
        val SignedOut = AuthSession(
            user = null,
            status = AuthStatus.SignedOut,
        )
    }
}

enum class AuthStatus {
    SignedOut,
    Authenticated,
}

data class LoginCredentials(
    val email: String,
    val password: String,
)

data class RegistrationData(
    val fullName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
)
