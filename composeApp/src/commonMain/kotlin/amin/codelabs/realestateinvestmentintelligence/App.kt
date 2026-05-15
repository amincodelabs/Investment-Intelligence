package amin.codelabs.realestateinvestmentintelligence

import amin.codelabs.realestateinvestmentintelligence.data.repository.DemoAuthRepository
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockAreaRepository
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockPropertyRepository
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockWatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.AppTheme
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.navigation.AppNavigationRoot
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun App(
    authRepository: AuthRepository = rememberPlatformAuthRepository(),
    areaRepository: AreaRepository = rememberPlatformAreaRepository(),
    propertyRepository: PropertyRepository = rememberPlatformPropertyRepository(),
    watchlistRepository: WatchlistRepository = rememberPlatformWatchlistRepository(),
) {
    AppTheme {
        AppNavigationRoot(
            authRepository = authRepository,
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        )
    }
}

@Preview
@Composable
fun AppPreview() {
    App(
        authRepository = DemoAuthRepository(),
        areaRepository = MockAreaRepository(),
        propertyRepository = MockPropertyRepository(),
        watchlistRepository = MockWatchlistRepository(),
    )
}
