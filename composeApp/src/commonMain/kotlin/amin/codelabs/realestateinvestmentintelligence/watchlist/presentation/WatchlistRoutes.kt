package amin.codelabs.realestateinvestmentintelligence.watchlist.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun WatchlistRoute(
    watchlistRepository: WatchlistRepository,
    onAreaClick: (String) -> Unit,
    onPropertyClick: (String) -> Unit,
) {
    val viewModel = rememberWatchlistViewModel(watchlistRepository = watchlistRepository)
    val effect = viewModel.effect

    LaunchedEffect(Unit) {
        viewModel.onEvent(WatchlistUiEvent.LoadWatchlist)
    }

    WatchlistScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
    )

    LaunchedEffect(effect) {
        when (val currentEffect = effect) {
            is WatchlistUiEffect.NavigateToAreaDetails -> onAreaClick(currentEffect.areaId)
            is WatchlistUiEffect.NavigateToPropertyDetails -> onPropertyClick(currentEffect.propertyId)
            null -> Unit
        }
        if (effect != null) {
            viewModel.consumeEffect()
        }
    }
}
