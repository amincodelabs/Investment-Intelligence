package amin.codelabs.realestateinvestmentintelligence.di

import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetPropertyDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PropertyDetailsModule {
    @Provides
    fun provideGetPropertyDetailsUseCase(
        propertyRepository: PropertyRepository,
        watchlistRepository: WatchlistRepository,
    ): GetPropertyDetailsUseCase {
        return GetPropertyDetailsUseCase(
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        )
    }
}
