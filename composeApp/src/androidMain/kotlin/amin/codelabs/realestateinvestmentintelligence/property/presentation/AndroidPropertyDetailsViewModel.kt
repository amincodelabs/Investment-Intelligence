package amin.codelabs.realestateinvestmentintelligence.property.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetPropertyDetailsUseCase
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidPropertyDetailsViewModel @Inject constructor(
    getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
    watchlistRepository: WatchlistRepository,
) : ViewModel(), PropertyDetailsViewModel {
    private val delegate = DefaultPropertyDetailsViewModel(
        getPropertyDetailsUseCase = getPropertyDetailsUseCase,
        watchlistRepository = watchlistRepository,
    )

    override val state: PropertyDetailsUiState
        get() = delegate.state

    override val effect: PropertyDetailsUiEffect?
        get() = delegate.effect

    override fun onEvent(event: PropertyDetailsUiEvent) {
        delegate.onEvent(event)
    }

    override fun consumeEffect() {
        delegate.consumeEffect()
    }
}

@Composable
internal actual fun rememberPropertyDetailsViewModel(
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
): PropertyDetailsViewModel = hiltViewModel<AndroidPropertyDetailsViewModel>()
