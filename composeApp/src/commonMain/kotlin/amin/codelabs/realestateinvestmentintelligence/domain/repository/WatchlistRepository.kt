package amin.codelabs.realestateinvestmentintelligence.domain.repository

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property

interface WatchlistRepository {
    fun getSavedProperties(): RepositoryResult<List<Property>>

    fun getSavedAreas(): RepositoryResult<List<Area>>

    fun saveProperty(propertyId: String): RepositoryResult<Unit>

    fun removeProperty(propertyId: String): RepositoryResult<Unit>

    fun saveArea(areaId: String): RepositoryResult<Unit>

    fun removeArea(areaId: String): RepositoryResult<Unit>
}
