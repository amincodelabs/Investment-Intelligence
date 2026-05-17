package amin.codelabs.realestateinvestmentintelligence.watchlist.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberWatchlistViewModel(
    watchlistRepository: WatchlistRepository,
): WatchlistViewModel {
    return remember(watchlistRepository) {
        DefaultWatchlistViewModel(watchlistRepository = watchlistRepository)
    }
}
