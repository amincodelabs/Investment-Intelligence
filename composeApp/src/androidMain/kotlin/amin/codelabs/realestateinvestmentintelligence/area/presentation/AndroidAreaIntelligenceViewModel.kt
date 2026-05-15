package amin.codelabs.realestateinvestmentintelligence.area.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaDetailsUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaListUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaPropertiesUseCase
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidAreaIntelligenceViewModel @Inject constructor(
    getAreaListUseCase: GetAreaListUseCase,
    getAreaDetailsUseCase: GetAreaDetailsUseCase,
    getAreaPropertiesUseCase: GetAreaPropertiesUseCase,
    watchlistRepository: WatchlistRepository,
) : ViewModel(), AreaIntelligenceViewModel {
    private val delegate = DefaultAreaIntelligenceViewModel(
        getAreaListUseCase = getAreaListUseCase,
        getAreaDetailsUseCase = getAreaDetailsUseCase,
        getAreaPropertiesUseCase = getAreaPropertiesUseCase,
        watchlistRepository = watchlistRepository,
    )

    override val state: AreaIntelligenceUiState
        get() = delegate.state

    override val effect: AreaIntelligenceUiEffect?
        get() = delegate.effect

    override fun onEvent(event: AreaIntelligenceUiEvent) {
        delegate.onEvent(event)
    }

    override fun consumeEffect() {
        delegate.consumeEffect()
    }
}

@Composable
internal actual fun rememberAreaIntelligenceViewModel(
    areaRepository: AreaRepository,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
): AreaIntelligenceViewModel = hiltViewModel<AndroidAreaIntelligenceViewModel>()
