package amin.codelabs.realestateinvestmentintelligence

import amin.codelabs.realestateinvestmentintelligence.data.repository.DemoAuthRepository
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.AppTheme
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.navigation.AppNavigationRoot
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun App(
    authRepository: AuthRepository = rememberPlatformAuthRepository(),
) {
    AppTheme {
        AppNavigationRoot(
            authRepository = authRepository,
        )
    }
}

@Preview
@Composable
fun AppPreview() {
    App(
        authRepository = DemoAuthRepository(),
    )
}
