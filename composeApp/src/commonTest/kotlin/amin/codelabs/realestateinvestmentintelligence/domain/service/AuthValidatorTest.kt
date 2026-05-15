package amin.codelabs.realestateinvestmentintelligence.domain.service

import amin.codelabs.realestateinvestmentintelligence.domain.model.RegistrationData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthValidatorTest {
    @Test
    fun `validateRegistration returns no errors for valid registration`() {
        val errors = AuthValidator.validateRegistration(validRegistration())

        assertTrue(errors.isEmpty())
    }

    @Test
    fun `validateRegistration returns invalid email for malformed email`() {
        val errors = AuthValidator.validateRegistration(
            validRegistration(email = "not-an-email"),
        )

        assertEquals(listOf(AuthValidationError.InvalidEmail), errors)
    }

    @Test
    fun `validateRegistration returns required name for empty name`() {
        val errors = AuthValidator.validateRegistration(
            validRegistration(fullName = " "),
        )

        assertEquals(listOf(AuthValidationError.RequiredName), errors)
    }

    @Test
    fun `validateRegistration returns password too short for short password`() {
        val errors = AuthValidator.validateRegistration(
            validRegistration(
                password = "short",
                confirmPassword = "short",
            ),
        )

        assertEquals(listOf(AuthValidationError.PasswordTooShort), errors)
    }

    @Test
    fun `validateRegistration returns password confirmation mismatch when passwords differ`() {
        val errors = AuthValidator.validateRegistration(
            validRegistration(
                password = "password123",
                confirmPassword = "different123",
            ),
        )

        assertEquals(listOf(AuthValidationError.PasswordConfirmationMismatch), errors)
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
}
