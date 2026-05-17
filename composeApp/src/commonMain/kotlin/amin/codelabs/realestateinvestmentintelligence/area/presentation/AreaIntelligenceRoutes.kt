package amin.codelabs.realestateinvestmentintelligence.area.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun AreaIntelligenceRoute(
    areaId: String?,
    areaRepository: AreaRepository,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
    onAreaClick: (String) -> Unit,
    onPropertyClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel = rememberAreaIntelligenceViewModel(
        areaRepository = areaRepository,
        propertyRepository = propertyRepository,
        watchlistRepository = watchlistRepository,
    )
    val effect = viewModel.effect

    LaunchedEffect(areaId) {
        if (areaId == null) {
            viewModel.onEvent(AreaIntelligenceUiEvent.LoadAreas)
        } else {
            viewModel.onEvent(AreaIntelligenceUiEvent.LoadAreaDetails(areaId))
        }
    }

    AreaIntelligenceScreen(
        state = viewModel.state,
        detailMode = areaId != null,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
    )

    LaunchedEffect(effect) {
        when (val currentEffect = effect) {
            is AreaIntelligenceUiEffect.NavigateToAreaDetails -> onAreaClick(currentEffect.areaId)
            is AreaIntelligenceUiEffect.NavigateToPropertyDetails -> onPropertyClick(currentEffect.propertyId)
            null -> Unit
        }
        if (effect != null) {
            viewModel.consumeEffect()
        }
    }
}
