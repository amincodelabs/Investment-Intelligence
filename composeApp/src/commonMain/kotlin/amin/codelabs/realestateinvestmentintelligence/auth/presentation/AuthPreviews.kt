package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.AppTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Login Light")
@Composable
private fun LoginScreenLightPreview() {
    AppTheme(darkTheme = false) {
        LoginScreen(
            state = LoginUiState(
                email = "investor@example.com",
                password = "password123",
            ),
            onEvent = {},
        )
    }
}

@Preview(name = "Login Dark")
@Composable
private fun LoginScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        LoginScreen(
            state = LoginUiState(
                email = "investor@example.com",
                isLoading = true,
            ),
            onEvent = {},
        )
    }
}

@Preview(name = "Register Light")
@Composable
private fun RegisterScreenLightPreview() {
    AppTheme(darkTheme = false) {
        RegisterScreen(
            state = RegisterUiState(
                fullName = "Amina Ali",
                email = "amina@example.com",
                password = "password123",
                confirmPassword = "password123",
            ),
            onEvent = {},
        )
    }
}

@Preview(name = "Register Dark")
@Composable
private fun RegisterScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        RegisterScreen(
            state = RegisterUiState(
                fullName = "",
                email = "invalid-email",
                password = "short",
                confirmPassword = "mismatch",
                emailError = "Enter a valid email address.",
                passwordError = "Use at least 8 characters.",
                confirmPasswordError = "Passwords do not match.",
            ),
            onEvent = {},
        )
    }
}
