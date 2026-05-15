package amin.codelabs.realestateinvestmentintelligence.di

import amin.codelabs.realestateinvestmentintelligence.data.mock.MockAreaDataSource
import amin.codelabs.realestateinvestmentintelligence.data.mock.MockPropertyDataSource
import amin.codelabs.realestateinvestmentintelligence.data.mock.MockWatchlistDataSource
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockAreaRepository
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockPropertyRepository
import amin.codelabs.realestateinvestmentintelligence.data.repository.MockWatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetDashboardOverviewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {
    @Provides
    @Singleton
    fun provideMockAreaDataSource(): MockAreaDataSource = MockAreaDataSource()

    @Provides
    @Singleton
    fun provideMockPropertyDataSource(): MockPropertyDataSource = MockPropertyDataSource()

    @Provides
    @Singleton
    fun provideMockWatchlistDataSource(): MockWatchlistDataSource = MockWatchlistDataSource()

    @Provides
    @Singleton
    fun provideAreaRepository(areaDataSource: MockAreaDataSource): AreaRepository {
        return MockAreaRepository(areaDataSource = areaDataSource)
    }

    @Provides
    @Singleton
    fun providePropertyRepository(propertyDataSource: MockPropertyDataSource): PropertyRepository {
        return MockPropertyRepository(propertyDataSource = propertyDataSource)
    }

    @Provides
    @Singleton
    fun provideWatchlistRepository(
        watchlistDataSource: MockWatchlistDataSource,
        propertyDataSource: MockPropertyDataSource,
        areaDataSource: MockAreaDataSource,
    ): WatchlistRepository {
        return MockWatchlistRepository(
            watchlistDataSource = watchlistDataSource,
            propertyDataSource = propertyDataSource,
            areaDataSource = areaDataSource,
        )
    }

    @Provides
    fun provideGetDashboardOverviewUseCase(
        areaRepository: AreaRepository,
        propertyRepository: PropertyRepository,
        watchlistRepository: WatchlistRepository,
    ): GetDashboardOverviewUseCase {
        return GetDashboardOverviewUseCase(
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        )
    }
}
