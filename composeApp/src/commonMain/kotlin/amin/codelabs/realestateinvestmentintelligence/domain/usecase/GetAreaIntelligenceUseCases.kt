package amin.codelabs.realestateinvestmentintelligence.domain.usecase

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository

class GetAreaListUseCase(
    private val areaRepository: AreaRepository,
    private val watchlistRepository: WatchlistRepository,
) {
    fun execute(): AreaListResult {
        val areas = when (val result = areaRepository.getAllAreas()) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> return AreaListResult.Failure(result.error)
        }
        val savedAreaIds = when (val result = watchlistRepository.getSavedAreas()) {
            is RepositoryResult.Success -> result.value.map { it.id }.toSet()
            is RepositoryResult.Failure -> return AreaListResult.Failure(result.error)
        }

        return AreaListResult.Success(
            areas = areas.map { area ->
                AreaWithWatchlistStatus(
                    area = area,
                    isSaved = area.id in savedAreaIds,
                )
            },
        )
    }
}

class GetAreaDetailsUseCase(
    private val areaRepository: AreaRepository,
    private val propertyRepository: PropertyRepository,
    private val watchlistRepository: WatchlistRepository,
) {
    fun execute(areaId: String): AreaDetailsResult {
        if (areaId.isBlank()) {
            return AreaDetailsResult.Failure(RepositoryError.NotFound)
        }

        val area = when (val result = areaRepository.getAreaById(areaId)) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> return AreaDetailsResult.Failure(result.error)
        }
        val properties = when (val result = propertyRepository.getPropertiesByAreaId(areaId)) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> {
                if (result.error == RepositoryError.NotFound) {
                    emptyList()
                } else {
                    return AreaDetailsResult.Failure(result.error)
                }
            }
        }
        val savedAreaIds = when (val result = watchlistRepository.getSavedAreas()) {
            is RepositoryResult.Success -> result.value.map { it.id }.toSet()
            is RepositoryResult.Failure -> return AreaDetailsResult.Failure(result.error)
        }

        return AreaDetailsResult.Success(
            details = AreaDetails(
                area = area,
                properties = properties,
                isSaved = area.id in savedAreaIds,
            ),
        )
    }
}

class GetAreaPropertiesUseCase(
    private val propertyRepository: PropertyRepository,
) {
    fun execute(areaId: String): AreaPropertiesResult {
        if (areaId.isBlank()) {
            return AreaPropertiesResult.Failure(RepositoryError.NotFound)
        }

        return when (val result = propertyRepository.getPropertiesByAreaId(areaId)) {
            is RepositoryResult.Success -> AreaPropertiesResult.Success(result.value)
            is RepositoryResult.Failure -> {
                if (result.error == RepositoryError.NotFound) {
                    AreaPropertiesResult.Success(emptyList())
                } else {
                    AreaPropertiesResult.Failure(result.error)
                }
            }
        }
    }
}

data class AreaWithWatchlistStatus(
    val area: Area,
    val isSaved: Boolean,
)

data class AreaDetails(
    val area: Area,
    val properties: List<Property>,
    val isSaved: Boolean,
)

sealed interface AreaListResult {
    data class Success(
        val areas: List<AreaWithWatchlistStatus>,
    ) : AreaListResult

    data class Failure(
        val error: RepositoryError,
    ) : AreaListResult
}

sealed interface AreaDetailsResult {
    data class Success(
        val details: AreaDetails,
    ) : AreaDetailsResult

    data class Failure(
        val error: RepositoryError,
    ) : AreaDetailsResult
}

sealed interface AreaPropertiesResult {
    data class Success(
        val properties: List<Property>,
    ) : AreaPropertiesResult

    data class Failure(
        val error: RepositoryError,
    ) : AreaPropertiesResult
}
