package amin.codelabs.realestateinvestmentintelligence

import amin.codelabs.realestateinvestmentintelligence.data.repository.DemoAuthRepository
import amin.codelabs.realestateinvestmentintelligence.data.mock.MockAreaDataSource
import amin.codelabs.realestateinvestmentintelligence.data.mock.MockPropertyDataSource
import amin.codelabs.realestateinvestmentintelligence.data.mock.MockWatchlistDataSource
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockAreaRepository
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockPropertyRepository
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockWatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberPlatformAuthRepository(): AuthRepository = remember {
    DemoAuthRepository()
}

@Composable
internal actual fun rememberPlatformAreaRepository(): AreaRepository = remember {
    IosDemoRepositories.areaRepository
}

@Composable
internal actual fun rememberPlatformPropertyRepository(): PropertyRepository = remember {
    IosDemoRepositories.propertyRepository
}

@Composable
internal actual fun rememberPlatformWatchlistRepository(): WatchlistRepository = remember {
    IosDemoRepositories.watchlistRepository
}

private object IosDemoRepositories {
    private val areaDataSource = MockAreaDataSource()
    private val propertyDataSource = MockPropertyDataSource()
    private val watchlistDataSource = MockWatchlistDataSource()

    val areaRepository: AreaRepository = MockAreaRepository(areaDataSource)
    val propertyRepository: PropertyRepository = MockPropertyRepository(propertyDataSource)
    val watchlistRepository: WatchlistRepository = MockWatchlistRepository(
        watchlistDataSource = watchlistDataSource,
        propertyDataSource = propertyDataSource,
        areaDataSource = areaDataSource,
    )
}
