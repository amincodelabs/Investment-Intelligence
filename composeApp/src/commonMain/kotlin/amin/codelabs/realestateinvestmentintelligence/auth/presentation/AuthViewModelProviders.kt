package amin.codelabs.realestateinvestmentintelligence.auth.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberLoginViewModel(
    authRepository: AuthRepository,
): LoginViewModel

@Composable
internal expect fun rememberRegisterViewModel(
    authRepository: AuthRepository,
): RegisterViewModel
