package amin.codelabs.realestateinvestmentintelligence.domain.repository

import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthSession
import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.LoginCredentials
import amin.codelabs.realestateinvestmentintelligence.domain.model.RegistrationData
import amin.codelabs.realestateinvestmentintelligence.domain.service.AuthValidationError

interface AuthRepository {
    fun register(data: RegistrationData): AuthResult<AuthSession>

    fun login(credentials: LoginCredentials): AuthResult<AuthSession>

    fun logout(): AuthResult<Unit>

    fun getCurrentSession(): AuthSession

    fun getAuthStatus(): AuthStatus
}

sealed interface AuthResult<out T> {
    data class Success<T>(
        val value: T,
    ) : AuthResult<T>

    data class Failure(
        val error: AuthError,
    ) : AuthResult<Nothing>
}

sealed interface AuthError {
    data class ValidationFailed(
        val errors: List<AuthValidationError>,
    ) : AuthError

    data object InvalidCredentials : AuthError

    data object EmailAlreadyRegistered : AuthError
}
