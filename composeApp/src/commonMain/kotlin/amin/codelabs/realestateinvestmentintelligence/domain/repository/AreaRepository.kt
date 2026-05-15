package amin.codelabs.realestateinvestmentintelligence.domain.repository

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area

interface AreaRepository {
    fun getAllAreas(): RepositoryResult<List<Area>>

    fun getAreaById(id: String): RepositoryResult<Area>
}
