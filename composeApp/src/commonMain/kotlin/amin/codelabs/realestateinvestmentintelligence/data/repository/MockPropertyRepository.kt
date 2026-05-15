package amin.codelabs.realestateinvestmentintelligence.data.repository

import amin.codelabs.realestateinvestmentintelligence.data.mock.MockPropertyDataSource
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult

class MockPropertyRepository(
    private val propertyDataSource: MockPropertyDataSource = MockPropertyDataSource(),
) : PropertyRepository {
    override fun getAllProperties(): RepositoryResult<List<Property>> {
        return RepositoryResult.Success(propertyDataSource.getAllProperties())
    }

    override fun getPropertyById(id: String): RepositoryResult<Property> {
        val property = propertyDataSource.getPropertyById(id)
            ?: return RepositoryResult.Failure(RepositoryError.NotFound)

        return RepositoryResult.Success(property)
    }

    override fun getPropertiesByAreaId(areaId: String): RepositoryResult<List<Property>> {
        val properties = propertyDataSource.getPropertiesByAreaId(areaId)
        if (properties.isEmpty()) {
            return RepositoryResult.Failure(RepositoryError.NotFound)
        }

        return RepositoryResult.Success(properties)
    }
}
