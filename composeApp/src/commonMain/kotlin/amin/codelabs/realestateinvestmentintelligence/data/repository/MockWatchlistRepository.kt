package amin.codelabs.realestateinvestmentintelligence.data.repository

import amin.codelabs.realestateinvestmentintelligence.data.mock.MockAreaDataSource
import amin.codelabs.realestateinvestmentintelligence.data.mock.MockPropertyDataSource
import amin.codelabs.realestateinvestmentintelligence.data.mock.MockWatchlistDataSource
import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository

class MockWatchlistRepository(
    private val watchlistDataSource: MockWatchlistDataSource = MockWatchlistDataSource(),
    private val propertyDataSource: MockPropertyDataSource = MockPropertyDataSource(),
    private val areaDataSource: MockAreaDataSource = MockAreaDataSource(),
) : WatchlistRepository {
    override fun getSavedProperties(): RepositoryResult<List<Property>> {
        val properties = watchlistDataSource.getSavedPropertyIds().mapNotNull { propertyId ->
            propertyDataSource.getPropertyById(propertyId)
        }

        return RepositoryResult.Success(properties)
    }

    override fun getSavedAreas(): RepositoryResult<List<Area>> {
        val areas = watchlistDataSource.getSavedAreaIds().mapNotNull { areaId ->
            areaDataSource.getAreaById(areaId)
        }

        return RepositoryResult.Success(areas)
    }

    override fun saveProperty(propertyId: String): RepositoryResult<Unit> {
        if (propertyDataSource.getPropertyById(propertyId) == null) {
            return RepositoryResult.Failure(RepositoryError.NotFound)
        }
        if (watchlistDataSource.containsProperty(propertyId)) {
            return RepositoryResult.Failure(RepositoryError.AlreadyExists)
        }

        watchlistDataSource.saveProperty(propertyId)
        return RepositoryResult.Success(Unit)
    }

    override fun removeProperty(propertyId: String): RepositoryResult<Unit> {
        if (!watchlistDataSource.removeProperty(propertyId)) {
            return RepositoryResult.Failure(RepositoryError.NotFound)
        }

        return RepositoryResult.Success(Unit)
    }

    override fun saveArea(areaId: String): RepositoryResult<Unit> {
        if (areaDataSource.getAreaById(areaId) == null) {
            return RepositoryResult.Failure(RepositoryError.NotFound)
        }
        if (watchlistDataSource.containsArea(areaId)) {
            return RepositoryResult.Failure(RepositoryError.AlreadyExists)
        }

        watchlistDataSource.saveArea(areaId)
        return RepositoryResult.Success(Unit)
    }

    override fun removeArea(areaId: String): RepositoryResult<Unit> {
        if (!watchlistDataSource.removeArea(areaId)) {
            return RepositoryResult.Failure(RepositoryError.NotFound)
        }

        return RepositoryResult.Success(Unit)
    }
}
