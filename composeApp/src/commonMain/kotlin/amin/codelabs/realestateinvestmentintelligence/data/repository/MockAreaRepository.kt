package amin.codelabs.realestateinvestmentintelligence.data.repository

import amin.codelabs.realestateinvestmentintelligence.data.mock.MockAreaDataSource
import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult

class MockAreaRepository(
    private val areaDataSource: MockAreaDataSource = MockAreaDataSource(),
) : AreaRepository {
    override fun getAllAreas(): RepositoryResult<List<Area>> {
        return RepositoryResult.Success(areaDataSource.getAllAreas())
    }

    override fun getAreaById(id: String): RepositoryResult<Area> {
        val area = areaDataSource.getAreaById(id)
            ?: return RepositoryResult.Failure(RepositoryError.NotFound)

        return RepositoryResult.Success(area)
    }
}
