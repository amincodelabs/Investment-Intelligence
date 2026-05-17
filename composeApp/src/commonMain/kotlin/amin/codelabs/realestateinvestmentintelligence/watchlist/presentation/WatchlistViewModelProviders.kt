package amin.codelabs.realestateinvestmentintelligence.watchlist.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberWatchlistViewModel(
    watchlistRepository: WatchlistRepository,
): WatchlistViewModel
