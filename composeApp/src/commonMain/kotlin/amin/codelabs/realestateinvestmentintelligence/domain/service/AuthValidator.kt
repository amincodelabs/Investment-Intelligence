package amin.codelabs.realestateinvestmentintelligence.domain.service

import amin.codelabs.realestateinvestmentintelligence.domain.model.LoginCredentials
import amin.codelabs.realestateinvestmentintelligence.domain.model.RegistrationData

object AuthValidator {
    fun validateRegistration(data: RegistrationData): List<AuthValidationError> {
        val errors = mutableListOf<AuthValidationError>()

        if (data.fullName.isBlank()) {
            errors += AuthValidationError.RequiredName
        }
        if (!isValidEmail(data.email)) {
            errors += AuthValidationError.InvalidEmail
        }
        if (data.password.isBlank()) {
            errors += AuthValidationError.RequiredPassword
        } else if (data.password.length < MIN_PASSWORD_LENGTH) {
            errors += AuthValidationError.PasswordTooShort
        }
        if (data.password != data.confirmPassword) {
            errors += AuthValidationError.PasswordConfirmationMismatch
        }

        return errors
    }

    fun validateLogin(credentials: LoginCredentials): List<AuthValidationError> {
        val errors = mutableListOf<AuthValidationError>()

        if (!isValidEmail(credentials.email)) {
            errors += AuthValidationError.InvalidEmail
        }
        if (credentials.password.isBlank()) {
            errors += AuthValidationError.RequiredPassword
        }

        return errors
    }

    private fun isValidEmail(email: String): Boolean {
        val trimmed = email.trim()
        val atIndex = trimmed.indexOf('@')
        val lastDotIndex = trimmed.lastIndexOf('.')

        return trimmed.isNotBlank() &&
            atIndex > 0 &&
            lastDotIndex > atIndex + 1 &&
            lastDotIndex < trimmed.lastIndex
    }

    const val MIN_PASSWORD_LENGTH = 8
}

enum class AuthValidationError {
    RequiredName,
    InvalidEmail,
    RequiredPassword,
    PasswordTooShort,
    PasswordConfirmationMismatch,
}
