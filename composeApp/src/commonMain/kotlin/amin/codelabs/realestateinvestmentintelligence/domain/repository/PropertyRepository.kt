package amin.codelabs.realestateinvestmentintelligence.domain.repository

import amin.codelabs.realestateinvestmentintelligence.domain.model.Property

interface PropertyRepository {
    fun getAllProperties(): RepositoryResult<List<Property>>

    fun getPropertyById(id: String): RepositoryResult<Property>

    fun getPropertiesByAreaId(areaId: String): RepositoryResult<List<Property>>
}
