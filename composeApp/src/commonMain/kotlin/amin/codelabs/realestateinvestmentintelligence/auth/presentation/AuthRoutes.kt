package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun LoginRoute(
    authRepository: AuthRepository,
    onRegisterClick: () -> Unit,
    onAuthenticated: () -> Unit,
) {
    val viewModel = rememberLoginViewModel(authRepository = authRepository)
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
    val viewModel = rememberRegisterViewModel(authRepository = authRepository)
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
