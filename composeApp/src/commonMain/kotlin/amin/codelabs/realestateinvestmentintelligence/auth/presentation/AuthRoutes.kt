package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun LoginRoute(
    authRepository: AuthRepository,
    onRegisterClick: () -> Unit,
    onAuthenticated: () -> Unit,
) {
    val viewModel = remember(authRepository) {
        LoginViewModel(authRepository = authRepository)
    }
    val effect = viewModel.effect

    LoginScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
    )

    LaunchedEffect(effect) {
        when (effect) {
            LoginUiEffect.NavigateToMain -> onAuthenticated()
            LoginUiEffect.NavigateToRegister -> onRegisterClick()
            null -> Unit
        }
        if (effect != null) {
            viewModel.consumeEffect()
        }
    }
}

@Composable
fun RegisterRoute(
    authRepository: AuthRepository,
    onLoginClick: () -> Unit,
    onAuthenticated: () -> Unit,
) {
    val viewModel = remember(authRepository) {
        RegisterViewModel(authRepository = authRepository)
    }
    val effect = viewModel.effect

    RegisterScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
    )

    LaunchedEffect(effect) {
        when (effect) {
            RegisterUiEffect.NavigateToMain -> onAuthenticated()
            RegisterUiEffect.NavigateToLogin -> onLoginClick()
            null -> Unit
        }
        if (effect != null) {
            viewModel.consumeEffect()
        }
    }
}
