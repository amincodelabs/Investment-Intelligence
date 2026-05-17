package amin.codelabs.realestateinvestmentintelligence.property.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetPropertyDetailsUseCase
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberPropertyDetailsViewModel(
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
): PropertyDetailsViewModel = remember(propertyRepository, watchlistRepository) {
    DefaultPropertyDetailsViewModel(
        getPropertyDetailsUseCase = GetPropertyDetailsUseCase(
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        ),
        watchlistRepository = watchlistRepository,
    )
}
