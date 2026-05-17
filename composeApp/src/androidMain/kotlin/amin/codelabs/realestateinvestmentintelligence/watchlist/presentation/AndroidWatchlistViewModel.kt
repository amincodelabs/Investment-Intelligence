package amin.codelabs.realestateinvestmentintelligence.watchlist.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidWatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository,
) : ViewModel(), WatchlistViewModel {
    private val delegate = DefaultWatchlistViewModel(
        watchlistRepository = watchlistRepository,
    )

    override val state: WatchlistUiState
        get() = delegate.state

    override val effect: WatchlistUiEffect?
        get() = delegate.effect

    override fun onEvent(event: WatchlistUiEvent) {
        delegate.onEvent(event)
    }

    override fun consumeEffect() {
        delegate.consumeEffect()
    }
}

@Composable
internal actual fun rememberWatchlistViewModel(
    watchlistRepository: WatchlistRepository,
): WatchlistViewModel {
    return hiltViewModel<AndroidWatchlistViewModel>()
}
