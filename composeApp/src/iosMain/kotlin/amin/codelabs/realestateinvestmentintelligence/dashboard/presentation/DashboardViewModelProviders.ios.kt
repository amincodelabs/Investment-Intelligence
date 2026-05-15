package amin.codelabs.realestateinvestmentintelligence.dashboard.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetDashboardOverviewUseCase
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberDashboardViewModel(
    areaRepository: AreaRepository,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
): DashboardViewModel = remember(areaRepository, propertyRepository, watchlistRepository) {
    DefaultDashboardViewModel(
        getDashboardOverviewUseCase = GetDashboardOverviewUseCase(
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        ),
    )
}
