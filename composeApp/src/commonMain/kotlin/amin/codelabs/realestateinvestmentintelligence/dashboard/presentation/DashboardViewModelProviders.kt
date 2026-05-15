package amin.codelabs.realestateinvestmentintelligence.dashboard.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberDashboardViewModel(
    areaRepository: AreaRepository,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
): DashboardViewModel
