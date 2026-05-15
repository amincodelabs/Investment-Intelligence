package amin.codelabs.realestateinvestmentintelligence.area.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaDetailsUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaListUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaPropertiesUseCase
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
internal actual fun rememberAreaIntelligenceViewModel(
    areaRepository: AreaRepository,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
): AreaIntelligenceViewModel = remember(areaRepository, propertyRepository, watchlistRepository) {
    DefaultAreaIntelligenceViewModel(
        getAreaListUseCase = GetAreaListUseCase(
            areaRepository = areaRepository,
            watchlistRepository = watchlistRepository,
        ),
        getAreaDetailsUseCase = GetAreaDetailsUseCase(
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        ),
        getAreaPropertiesUseCase = GetAreaPropertiesUseCase(
            propertyRepository = propertyRepository,
        ),
        watchlistRepository = watchlistRepository,
    )
}
