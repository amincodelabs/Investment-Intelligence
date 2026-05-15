package amin.codelabs.realestateinvestmentintelligence.di

import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaDetailsUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaListUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaPropertiesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AreaIntelligenceModule {
    @Provides
    fun provideGetAreaListUseCase(
        areaRepository: AreaRepository,
        watchlistRepository: WatchlistRepository,
    ): GetAreaListUseCase {
        return GetAreaListUseCase(
            areaRepository = areaRepository,
            watchlistRepository = watchlistRepository,
        )
    }

    @Provides
    fun provideGetAreaDetailsUseCase(
        areaRepository: AreaRepository,
        propertyRepository: PropertyRepository,
        watchlistRepository: WatchlistRepository,
    ): GetAreaDetailsUseCase {
        return GetAreaDetailsUseCase(
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        )
    }

    @Provides
    fun provideGetAreaPropertiesUseCase(
        propertyRepository: PropertyRepository,
    ): GetAreaPropertiesUseCase {
        return GetAreaPropertiesUseCase(
            propertyRepository = propertyRepository,
        )
    }
}
