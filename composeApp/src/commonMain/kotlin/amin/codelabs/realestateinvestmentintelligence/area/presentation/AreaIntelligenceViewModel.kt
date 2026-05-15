package amin.codelabs.realestateinvestmentintelligence.area.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.AreaDetails
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.AreaDetailsResult
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.AreaListResult
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.AreaPropertiesResult
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.AreaWithWatchlistStatus
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaDetailsUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaListUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaPropertiesUseCase
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface AreaIntelligenceViewModel {
    val state: AreaIntelligenceUiState
    val effect: AreaIntelligenceUiEffect?

    fun onEvent(event: AreaIntelligenceUiEvent)
    fun consumeEffect()
}

class DefaultAreaIntelligenceViewModel(
    private val getAreaListUseCase: GetAreaListUseCase,
    private val getAreaDetailsUseCase: GetAreaDetailsUseCase,
    private val getAreaPropertiesUseCase: GetAreaPropertiesUseCase,
    private val watchlistRepository: WatchlistRepository,
    private val logger: AreaIntelligenceLogger = NoOpAreaIntelligenceLogger,
) : AreaIntelligenceViewModel {
    override var state by mutableStateOf<AreaIntelligenceUiState>(AreaIntelligenceUiState.Loading)
        private set

    override var effect by mutableStateOf<AreaIntelligenceUiEffect?>(null)
        private set

    private var lastLoadEvent: AreaIntelligenceUiEvent = AreaIntelligenceUiEvent.LoadAreas

    override fun onEvent(event: AreaIntelligenceUiEvent) {
        when (event) {
            AreaIntelligenceUiEvent.LoadAreas -> loadAreas()
            AreaIntelligenceUiEvent.RetryClicked -> retry()
            is AreaIntelligenceUiEvent.LoadAreaDetails -> loadAreaDetails(event.areaId)
            is AreaIntelligenceUiEvent.LoadPropertiesByArea -> loadPropertiesByArea(event.areaId)
            is AreaIntelligenceUiEvent.AreaClicked -> {
                logger.log(AreaIntelligenceLogEvent.AreaSelected)
                effect = AreaIntelligenceUiEffect.NavigateToAreaDetails(event.areaId)
            }

            is AreaIntelligenceUiEvent.PropertyClicked -> {
                logger.log(AreaIntelligenceLogEvent.PropertySelected)
                effect = AreaIntelligenceUiEffect.NavigateToPropertyDetails(event.propertyId)
            }

            is AreaIntelligenceUiEvent.SaveAreaClicked -> saveArea(event.areaId)
            is AreaIntelligenceUiEvent.RemoveAreaClicked -> removeArea(event.areaId)
        }
    }

    override fun consumeEffect() {
        effect = null
    }

    private fun retry() {
        logger.log(AreaIntelligenceLogEvent.RetryStarted)
        when (val event = lastLoadEvent) {
            AreaIntelligenceUiEvent.LoadAreas -> loadAreas()
            is AreaIntelligenceUiEvent.LoadAreaDetails -> loadAreaDetails(event.areaId)
            is AreaIntelligenceUiEvent.LoadPropertiesByArea -> loadPropertiesByArea(event.areaId)
            AreaIntelligenceUiEvent.RetryClicked,
            is AreaIntelligenceUiEvent.AreaClicked,
            is AreaIntelligenceUiEvent.PropertyClicked,
            is AreaIntelligenceUiEvent.SaveAreaClicked,
            is AreaIntelligenceUiEvent.RemoveAreaClicked,
            -> loadAreas()
        }
    }

    private fun loadAreas() {
        lastLoadEvent = AreaIntelligenceUiEvent.LoadAreas
        logger.log(AreaIntelligenceLogEvent.LoadAreasStarted)
        state = AreaIntelligenceUiState.Loading

        when (val result = getAreaListUseCase.execute()) {
            is AreaListResult.Success -> {
                if (result.areas.isEmpty()) {
                    logger.log(AreaIntelligenceLogEvent.LoadedEmpty)
                    state = AreaIntelligenceUiState.Empty
                } else {
                    logger.log(AreaIntelligenceLogEvent.LoadAreasSucceeded)
                    state = AreaIntelligenceUiState.Content(
                        AreaIntelligenceContent(
                            areas = result.areas.map { it.toAreaListItemUi() },
                        ),
                    )
                }
            }

            is AreaListResult.Failure -> {
                logger.log(AreaIntelligenceLogEvent.LoadFailed)
                state = AreaIntelligenceUiState.Error(result.error.toAreaErrorMessage())
            }
        }
    }

    private fun loadAreaDetails(areaId: String) {
        lastLoadEvent = AreaIntelligenceUiEvent.LoadAreaDetails(areaId)
        logger.log(AreaIntelligenceLogEvent.LoadAreaDetailsStarted)
        val previousAreas = currentAreas()
        state = AreaIntelligenceUiState.Loading

        when (val result = getAreaDetailsUseCase.execute(areaId)) {
            is AreaDetailsResult.Success -> {
                logger.log(AreaIntelligenceLogEvent.LoadAreaDetailsSucceeded)
                state = AreaIntelligenceUiState.Content(
                    AreaIntelligenceContent(
                        areas = previousAreas,
                        selectedArea = result.details.toAreaDetailsUi(),
                        properties = result.details.properties.map { it.toAreaPropertyItemUi() },
                    ),
                )
            }

            is AreaDetailsResult.Failure -> {
                logger.log(AreaIntelligenceLogEvent.LoadFailed)
                state = AreaIntelligenceUiState.Error(result.error.toAreaErrorMessage())
            }
        }
    }

    private fun loadPropertiesByArea(areaId: String) {
        lastLoadEvent = AreaIntelligenceUiEvent.LoadPropertiesByArea(areaId)
        logger.log(AreaIntelligenceLogEvent.LoadPropertiesStarted)
        val previousAreas = currentAreas()
        val previousSelectedArea = currentSelectedArea()
        state = AreaIntelligenceUiState.Loading

        when (val result = getAreaPropertiesUseCase.execute(areaId)) {
            is AreaPropertiesResult.Success -> {
                logger.log(AreaIntelligenceLogEvent.LoadPropertiesSucceeded)
                state = AreaIntelligenceUiState.Content(
                    AreaIntelligenceContent(
                        areas = previousAreas,
                        selectedArea = previousSelectedArea,
                        properties = result.properties.map { it.toAreaPropertyItemUi() },
                    ),
                )
            }

            is AreaPropertiesResult.Failure -> {
                logger.log(AreaIntelligenceLogEvent.LoadFailed)
                state = AreaIntelligenceUiState.Error(result.error.toAreaErrorMessage())
            }
        }
    }

    private fun saveArea(areaId: String) {
        logger.log(AreaIntelligenceLogEvent.SaveAreaStarted)
        when (val result = watchlistRepository.saveArea(areaId)) {
            is RepositoryResult.Success -> {
                logger.log(AreaIntelligenceLogEvent.SaveAreaSucceeded)
                updateSavedStatus(areaId = areaId, isSaved = true)
            }

            is RepositoryResult.Failure -> {
                logger.log(AreaIntelligenceLogEvent.WatchlistActionFailed)
                if (result.error == RepositoryError.AlreadyExists) {
                    updateSavedStatus(areaId = areaId, isSaved = true)
                } else {
                    state = AreaIntelligenceUiState.Error(result.error.toAreaErrorMessage())
                }
            }
        }
    }

    private fun removeArea(areaId: String) {
        logger.log(AreaIntelligenceLogEvent.RemoveAreaStarted)
        when (val result = watchlistRepository.removeArea(areaId)) {
            is RepositoryResult.Success -> {
                logger.log(AreaIntelligenceLogEvent.RemoveAreaSucceeded)
                updateSavedStatus(areaId = areaId, isSaved = false)
            }

            is RepositoryResult.Failure -> {
                logger.log(AreaIntelligenceLogEvent.WatchlistActionFailed)
                if (result.error == RepositoryError.NotFound) {
                    updateSavedStatus(areaId = areaId, isSaved = false)
                } else {
                    state = AreaIntelligenceUiState.Error(result.error.toAreaErrorMessage())
                }
            }
        }
    }

    private fun updateSavedStatus(areaId: String, isSaved: Boolean) {
        val content = (state as? AreaIntelligenceUiState.Content)?.content ?: return
        state = AreaIntelligenceUiState.Content(
            content.copy(
                areas = content.areas.map { area ->
                    if (area.id == areaId) area.copy(isSaved = isSaved) else area
                },
                selectedArea = content.selectedArea?.let { area ->
                    if (area.id == areaId) area.copy(isSaved = isSaved) else area
                },
            ),
        )
    }

    private fun currentAreas(): List<AreaListItemUi> {
        return (state as? AreaIntelligenceUiState.Content)?.content?.areas.orEmpty()
    }

    private fun currentSelectedArea(): AreaDetailsUi? {
        return (state as? AreaIntelligenceUiState.Content)?.content?.selectedArea
    }
}

sealed interface AreaIntelligenceUiState {
    data object Loading : AreaIntelligenceUiState
    data object Empty : AreaIntelligenceUiState

    data class Content(
        val content: AreaIntelligenceContent,
    ) : AreaIntelligenceUiState

    data class Error(
        val message: String,
    ) : AreaIntelligenceUiState
}

@Immutable
data class AreaIntelligenceContent(
    val areas: List<AreaListItemUi>,
    val selectedArea: AreaDetailsUi? = null,
    val properties: List<AreaPropertyItemUi> = emptyList(),
)

@Immutable
data class AreaListItemUi(
    val id: String,
    val name: String,
    val averagePriceAmount: Double?,
    val currency: String,
    val averageRentalYieldPercentage: Double?,
    val demandLabel: String?,
    val riskLabel: String?,
    val appreciationPotentialPercentage: Double?,
    val investmentScore: Int?,
    val isSaved: Boolean,
)

@Immutable
data class AreaDetailsUi(
    val id: String,
    val name: String,
    val averagePriceAmount: Double?,
    val currency: String,
    val averageRentalYieldPercentage: Double?,
    val demandLabel: String?,
    val riskLabel: String?,
    val appreciationPotentialPercentage: Double?,
    val investmentScore: Int?,
    val isSaved: Boolean,
)

@Immutable
data class AreaPropertyItemUi(
    val id: String,
    val title: String,
    val areaId: String,
    val priceAmount: Double,
    val currency: String,
    val sizeSqft: Double,
    val netRentalYieldPercentage: Double?,
    val grossRentalYieldPercentage: Double?,
    val investmentScore: Int?,
)

sealed interface AreaIntelligenceUiEvent {
    data object LoadAreas : AreaIntelligenceUiEvent
    data class LoadAreaDetails(val areaId: String) : AreaIntelligenceUiEvent
    data class LoadPropertiesByArea(val areaId: String) : AreaIntelligenceUiEvent
    data object RetryClicked : AreaIntelligenceUiEvent
    data class AreaClicked(val areaId: String) : AreaIntelligenceUiEvent
    data class PropertyClicked(val propertyId: String) : AreaIntelligenceUiEvent
    data class SaveAreaClicked(val areaId: String) : AreaIntelligenceUiEvent
    data class RemoveAreaClicked(val areaId: String) : AreaIntelligenceUiEvent
}

sealed interface AreaIntelligenceUiEffect {
    data class NavigateToAreaDetails(val areaId: String) : AreaIntelligenceUiEffect
    data class NavigateToPropertyDetails(val propertyId: String) : AreaIntelligenceUiEffect
}

interface AreaIntelligenceLogger {
    fun log(event: AreaIntelligenceLogEvent)
}

enum class AreaIntelligenceLogEvent {
    LoadAreasStarted,
    LoadAreasSucceeded,
    LoadAreaDetailsStarted,
    LoadAreaDetailsSucceeded,
    LoadPropertiesStarted,
    LoadPropertiesSucceeded,
    LoadedEmpty,
    LoadFailed,
    RetryStarted,
    AreaSelected,
    PropertySelected,
    SaveAreaStarted,
    SaveAreaSucceeded,
    RemoveAreaStarted,
    RemoveAreaSucceeded,
    WatchlistActionFailed,
}

object NoOpAreaIntelligenceLogger : AreaIntelligenceLogger {
    override fun log(event: AreaIntelligenceLogEvent) = Unit
}

private fun AreaWithWatchlistStatus.toAreaListItemUi(): AreaListItemUi = AreaListItemUi(
    id = area.id,
    name = area.name,
    averagePriceAmount = area.averagePropertyPrice?.amount,
    currency = area.averagePropertyPrice?.currency ?: DEFAULT_CURRENCY,
    averageRentalYieldPercentage = area.averageRentalYield?.percentage,
    demandLabel = area.demandLevel?.label(),
    riskLabel = area.riskLevel?.label(),
    appreciationPotentialPercentage = area.appreciationPotentialPercentage,
    investmentScore = area.investmentScore?.value,
    isSaved = isSaved,
)

private fun AreaDetails.toAreaDetailsUi(): AreaDetailsUi = AreaDetailsUi(
    id = area.id,
    name = area.name,
    averagePriceAmount = area.averagePropertyPrice?.amount,
    currency = area.averagePropertyPrice?.currency ?: DEFAULT_CURRENCY,
    averageRentalYieldPercentage = area.averageRentalYield?.percentage,
    demandLabel = area.demandLevel?.label(),
    riskLabel = area.riskLevel?.label(),
    appreciationPotentialPercentage = area.appreciationPotentialPercentage,
    investmentScore = area.investmentScore?.value,
    isSaved = isSaved,
)

private fun Property.toAreaPropertyItemUi(): AreaPropertyItemUi = AreaPropertyItemUi(
    id = id,
    title = title,
    areaId = areaId,
    priceAmount = price.amount,
    currency = price.currency,
    sizeSqft = sizeSqft,
    netRentalYieldPercentage = investmentMetrics?.netRentalYield?.percentage,
    grossRentalYieldPercentage = investmentMetrics?.grossRentalYield?.percentage,
    investmentScore = investmentScore?.value,
)

private fun DemandLevel.label(): String = when (this) {
    DemandLevel.Low -> "Low"
    DemandLevel.Medium -> "Medium"
    DemandLevel.High -> "High"
}

private fun RiskLevel.label(): String = when (this) {
    RiskLevel.Low -> "Low"
    RiskLevel.Medium -> "Medium"
    RiskLevel.High -> "High"
}

private fun RepositoryError.toAreaErrorMessage(): String = when (this) {
    RepositoryError.NotFound -> "Area data is not available yet."
    RepositoryError.AlreadyExists -> "This area is already saved."
    RepositoryError.Unknown -> "Unable to load area intelligence. Please try again."
}

private const val DEFAULT_CURRENCY = "AED"
