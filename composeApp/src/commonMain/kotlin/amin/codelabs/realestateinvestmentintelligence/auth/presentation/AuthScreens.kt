package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppErrorState
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppLoadingButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppPasswordTextField
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppSectionTitle
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTextField
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTertiaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTopBar
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
) {
    AuthScreenScaffold(
        title = "Sign in",
        subtitle = "Use your demo account to continue.",
        actionLabel = "Register",
        actionEnabled = !state.isLoading,
        onActionClick = { onEvent(LoginUiEvent.RegisterClicked) },
    ) {
        AppTextField(
            value = state.email,
            onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
            label = "Email",
            placeholder = "name@example.com",
            supportingText = state.emailError,
            isError = state.emailError != null,
            enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
            ),
        )
        AppPasswordTextField(
            value = state.password,
            onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
            label = "Password",
            supportingText = state.passwordError,
            isError = state.passwordError != null,
            enabled = !state.isLoading,
        )
        state.generalError?.let {
            AppErrorState(
                title = "Sign in failed",
                message = it,
            )
        }
        AppLoadingButton(
            text = "Sign in",
            onClick = { onEvent(LoginUiEvent.SubmitClicked) },
            loading = state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun RegisterScreen(
    state: RegisterUiState,
    onEvent: (RegisterUiEvent) -> Unit,
) {
    AuthScreenScaffold(
        title = "Create account",
        subtitle = "Create a local demo profile to explore the app.",
        actionLabel = "Login",
        actionEnabled = !state.isLoading,
        onActionClick = { onEvent(RegisterUiEvent.LoginClicked) },
    ) {
        AppTextField(
            value = state.fullName,
            onValueChange = { onEvent(RegisterUiEvent.FullNameChanged(it)) },
            label = "Full name",
            placeholder = "Your name",
            supportingText = state.fullNameError,
            isError = state.fullNameError != null,
            enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            ),
        )
        AppTextField(
            value = state.email,
            onValueChange = { onEvent(RegisterUiEvent.EmailChanged(it)) },
            label = "Email",
            placeholder = "name@example.com",
            supportingText = state.emailError,
            isError = state.emailError != null,
            enabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
            ),
        )
        AppPasswordTextField(
            value = state.password,
            onValueChange = { onEvent(RegisterUiEvent.PasswordChanged(it)) },
            label = "Password",
            supportingText = state.passwordError,
            isError = state.passwordError != null,
            enabled = !state.isLoading,
        )
        AppPasswordTextField(
            value = state.confirmPassword,
            onValueChange = { onEvent(RegisterUiEvent.ConfirmPasswordChanged(it)) },
            label = "Confirm password",
            supportingText = state.confirmPasswordError,
            isError = state.confirmPasswordError != null,
            enabled = !state.isLoading,
        )
        state.generalError?.let {
            AppErrorState(
                title = "Registration failed",
                message = it,
            )
        }
        AppLoadingButton(
            text = "Create account",
            onClick = { onEvent(RegisterUiEvent.SubmitClicked) },
            loading = state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun AuthScreenScaffold(
    title: String,
    subtitle: String,
    actionLabel: String,
    actionEnabled: Boolean,
    onActionClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding()
            .padding(MaterialTheme.appSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.lg),
    ) {
        AppTopBar(
            title = title,
            actions = {
                AppTertiaryButton(
                    text = actionLabel,
                    onClick = onActionClick,
                    enabled = actionEnabled,
                )
            },
        )
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
                AppSectionTitle(
                    title = title,
                    subtitle = subtitle,
                )
                content()
            }
        }
    }
}
