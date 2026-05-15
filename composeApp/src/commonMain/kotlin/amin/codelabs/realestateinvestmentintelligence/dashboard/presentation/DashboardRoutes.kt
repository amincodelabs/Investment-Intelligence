package amin.codelabs.realestateinvestmentintelligence.dashboard.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun DashboardRoute(
    areaRepository: AreaRepository,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
    onAreaClick: (String) -> Unit,
    onPropertyClick: (String) -> Unit,
    onWatchlistClick: () -> Unit,
) {
    val viewModel = rememberDashboardViewModel(
        areaRepository = areaRepository,
        propertyRepository = propertyRepository,
        watchlistRepository = watchlistRepository,
    )
    val effect = viewModel.effect

    LaunchedEffect(Unit) {
        viewModel.onEvent(DashboardUiEvent.LoadDashboard)
    }

    DashboardScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
    )

    LaunchedEffect(effect) {
        when (val currentEffect = effect) {
            is DashboardUiEffect.NavigateToAreaDetails -> onAreaClick(currentEffect.areaId)
            is DashboardUiEffect.NavigateToPropertyDetails -> onPropertyClick(currentEffect.propertyId)
            DashboardUiEffect.NavigateToWatchlist -> onWatchlistClick()
            null -> Unit
        }
        if (effect != null) {
            viewModel.consumeEffect()
        }
    }
}
