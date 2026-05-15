package amin.codelabs.realestateinvestmentintelligence

import amin.codelabs.realestateinvestmentintelligence.data.repository.DemoAuthRepository
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.AppTheme
import amin.codelabs.realestateinvestmentintelligence.navigation.AppNavigationRoot
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        AppNavigationRoot(
            authRepository = DemoAuthRepository(),
        )
    }
}
