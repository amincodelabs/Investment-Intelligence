package amin.codelabs.realestateinvestmentintelligence

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberPlatformAuthRepository(): AuthRepository
