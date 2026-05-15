package amin.codelabs.realestateinvestmentintelligence.data.repository

import amin.codelabs.realestateinvestmentintelligence.data.auth.AuthEventLogger
import amin.codelabs.realestateinvestmentintelligence.data.auth.AuthLogEvent
import amin.codelabs.realestateinvestmentintelligence.data.auth.DemoAuthDataSource
import amin.codelabs.realestateinvestmentintelligence.data.auth.NoOpAuthEventLogger
import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthSession
import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.Email
import amin.codelabs.realestateinvestmentintelligence.domain.model.LoginCredentials
import amin.codelabs.realestateinvestmentintelligence.domain.model.RegistrationData
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthResult
import amin.codelabs.realestateinvestmentintelligence.domain.service.AuthValidator

class DemoAuthRepository(
    private val authDataSource: DemoAuthDataSource = DemoAuthDataSource(),
    private val logger: AuthEventLogger = NoOpAuthEventLogger,
) : AuthRepository {
    private var currentSession: AuthSession = AuthSession.SignedOut

    override fun register(data: RegistrationData): AuthResult<AuthSession> {
        val validationErrors = AuthValidator.validateRegistration(data)
        if (validationErrors.isNotEmpty()) {
            logger.log(AuthLogEvent.RegistrationValidationFailed)
            return AuthResult.Failure(AuthError.ValidationFailed(validationErrors))
        }

        val email = Email(data.email)
        if (authDataSource.findUserByEmail(email) != null) {
            logger.log(AuthLogEvent.RegistrationRejectedDuplicateEmail)
            return AuthResult.Failure(AuthError.EmailAlreadyRegistered)
        }

        val record = authDataSource.saveUser(
            fullName = data.fullName,
            email = email,
            password = data.password,
        )
        currentSession = AuthSession(
            user = record.user,
            status = AuthStatus.Authenticated,
        )
        logger.log(AuthLogEvent.RegistrationSucceeded)

        return AuthResult.Success(currentSession)
    }

    override fun login(credentials: LoginCredentials): AuthResult<AuthSession> {
        val validationErrors = AuthValidator.validateLogin(credentials)
        if (validationErrors.isNotEmpty()) {
            logger.log(AuthLogEvent.LoginValidationFailed)
            return AuthResult.Failure(AuthError.ValidationFailed(validationErrors))
        }

        val record = authDataSource.findUserByEmail(Email(credentials.email))
        if (record == null || !record.matchesPassword(credentials.password)) {
            logger.log(AuthLogEvent.LoginRejectedInvalidCredentials)
            return AuthResult.Failure(AuthError.InvalidCredentials)
        }

        currentSession = AuthSession(
            user = record.user,
            status = AuthStatus.Authenticated,
        )
        logger.log(AuthLogEvent.LoginSucceeded)

        return AuthResult.Success(currentSession)
    }

    override fun logout(): AuthResult<Unit> {
        currentSession = AuthSession.SignedOut
        logger.log(AuthLogEvent.LogoutSucceeded)
        return AuthResult.Success(Unit)
    }

    override fun getCurrentSession(): AuthSession = currentSession

    override fun getAuthStatus(): AuthStatus = currentSession.status
}
