package amin.codelabs.realestateinvestmentintelligence.property.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun PropertyDetailsRoute(
    propertyId: String,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
    onAreaClick: (String) -> Unit,
    onComparisonClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel = rememberPropertyDetailsViewModel(
        propertyRepository = propertyRepository,
        watchlistRepository = watchlistRepository,
    )
    val effect = viewModel.effect

    LaunchedEffect(propertyId) {
        viewModel.onEvent(PropertyDetailsUiEvent.LoadProperty(propertyId))
    }

    PropertyDetailsScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
    )

    LaunchedEffect(effect) {
        when (val currentEffect = effect) {
            is PropertyDetailsUiEffect.NavigateToAreaDetails -> onAreaClick(currentEffect.areaId)
            PropertyDetailsUiEffect.NavigateToComparison -> onComparisonClick()
            null -> Unit
        }
        if (effect != null) {
            viewModel.consumeEffect()
        }
    }
}
