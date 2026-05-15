package amin.codelabs.realestateinvestmentintelligence

import amin.codelabs.realestateinvestmentintelligence.data.repository.DemoAuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberPlatformAuthRepository(): AuthRepository = remember {
    DemoAuthRepository()
}
