package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberLoginViewModel(
    authRepository: AuthRepository,
): LoginViewModel = remember(authRepository) {
    DefaultLoginViewModel(authRepository = authRepository)
}

@Composable
internal actual fun rememberRegisterViewModel(
    authRepository: AuthRepository,
): RegisterViewModel = remember(authRepository) {
    DefaultRegisterViewModel(authRepository = authRepository)
}
