package amin.codelabs.realestateinvestmentintelligence.property.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Composable

@Composable
internal expect fun rememberPropertyDetailsViewModel(
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
): PropertyDetailsViewModel
