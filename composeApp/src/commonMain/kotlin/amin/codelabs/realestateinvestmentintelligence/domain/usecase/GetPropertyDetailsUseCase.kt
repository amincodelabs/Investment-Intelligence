package amin.codelabs.realestateinvestmentintelligence.domain.usecase

import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository

class GetPropertyDetailsUseCase(
    private val propertyRepository: PropertyRepository,
    private val watchlistRepository: WatchlistRepository,
) {
    fun execute(propertyId: String): PropertyDetailsResult {
        if (propertyId.isBlank()) {
            return PropertyDetailsResult.Failure(RepositoryError.NotFound)
        }

        val property = when (val result = propertyRepository.getPropertyById(propertyId)) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> return PropertyDetailsResult.Failure(result.error)
        }
        val savedPropertyIds = when (val result = watchlistRepository.getSavedProperties()) {
            is RepositoryResult.Success -> result.value.map { it.id }.toSet()
            is RepositoryResult.Failure -> return PropertyDetailsResult.Failure(result.error)
        }

        return PropertyDetailsResult.Success(
            PropertyDetails(
                property = property,
                isSaved = property.id in savedPropertyIds,
            ),
        )
    }
}

data class PropertyDetails(
    val property: Property,
    val isSaved: Boolean,
)

sealed interface PropertyDetailsResult {
    data class Success(
        val details: PropertyDetails,
    ) : PropertyDetailsResult

    data class Failure(
        val error: RepositoryError,
    ) : PropertyDetailsResult
}
