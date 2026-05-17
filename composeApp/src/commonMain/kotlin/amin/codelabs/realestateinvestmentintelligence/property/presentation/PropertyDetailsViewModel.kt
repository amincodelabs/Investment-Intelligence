package amin.codelabs.realestateinvestmentintelligence.property.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.CompletionStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.model.PropertyType
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetPropertyDetailsUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.PropertyDetails
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.PropertyDetailsResult
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface PropertyDetailsViewModel {
    val state: PropertyDetailsUiState
    val effect: PropertyDetailsUiEffect?

    fun onEvent(event: PropertyDetailsUiEvent)
    fun consumeEffect()
}

class DefaultPropertyDetailsViewModel(
    private val getPropertyDetailsUseCase: GetPropertyDetailsUseCase,
    private val watchlistRepository: WatchlistRepository,
    private val logger: PropertyDetailsLogger = NoOpPropertyDetailsLogger,
) : PropertyDetailsViewModel {
    override var state by mutableStateOf<PropertyDetailsUiState>(PropertyDetailsUiState.Loading)
        private set

    override var effect by mutableStateOf<PropertyDetailsUiEffect?>(null)
        private set

    private var lastPropertyId: String? = null

    override fun onEvent(event: PropertyDetailsUiEvent) {
        when (event) {
            is PropertyDetailsUiEvent.LoadProperty -> loadProperty(event.propertyId)
            PropertyDetailsUiEvent.RetryClicked -> retry()
            is PropertyDetailsUiEvent.SavePropertyClicked -> saveProperty(event.propertyId)
            is PropertyDetailsUiEvent.RemovePropertyClicked -> removeProperty(event.propertyId)
            is PropertyDetailsUiEvent.AddToComparisonClicked -> addToComparison(event.propertyId)
            is PropertyDetailsUiEvent.RemoveFromComparisonClicked -> removeFromComparison(event.propertyId)
            is PropertyDetailsUiEvent.AreaClicked -> {
                logger.log(PropertyDetailsLogEvent.AreaSelected)
                effect = PropertyDetailsUiEffect.NavigateToAreaDetails(event.areaId)
            }

            PropertyDetailsUiEvent.ComparisonClicked -> {
                logger.log(PropertyDetailsLogEvent.ComparisonSelected)
                effect = PropertyDetailsUiEffect.NavigateToComparison
            }
        }
    }

    override fun consumeEffect() {
        effect = null
    }

    private fun retry() {
        logger.log(PropertyDetailsLogEvent.RetryStarted)
        val propertyId = lastPropertyId
        if (propertyId == null) {
            state = PropertyDetailsUiState.Error("Property data is not available yet.")
        } else {
            loadProperty(propertyId)
        }
    }

    private fun loadProperty(propertyId: String) {
        lastPropertyId = propertyId
        logger.log(PropertyDetailsLogEvent.LoadStarted)
        if (propertyId.isBlank()) {
            state = PropertyDetailsUiState.Empty
            return
        }
        state = PropertyDetailsUiState.Loading

        when (val result = getPropertyDetailsUseCase.execute(propertyId)) {
            is PropertyDetailsResult.Success -> {
                logger.log(PropertyDetailsLogEvent.LoadSucceeded)
                state = PropertyDetailsUiState.Content(
                    content = result.details.toPropertyDetailsContent(),
                )
            }

            is PropertyDetailsResult.Failure -> {
                logger.log(PropertyDetailsLogEvent.LoadFailed)
                state = PropertyDetailsUiState.Error(result.error.toPropertyDetailsErrorMessage())
            }
        }
    }

    private fun saveProperty(propertyId: String) {
        logger.log(PropertyDetailsLogEvent.SavePropertyStarted)
        when (val result = watchlistRepository.saveProperty(propertyId)) {
            is RepositoryResult.Success -> {
                logger.log(PropertyDetailsLogEvent.SavePropertySucceeded)
                updateSavedStatus(propertyId = propertyId, isSaved = true)
            }

            is RepositoryResult.Failure -> {
                logger.log(PropertyDetailsLogEvent.WatchlistActionFailed)
                if (result.error == RepositoryError.AlreadyExists) {
                    updateSavedStatus(propertyId = propertyId, isSaved = true)
                } else {
                    state = PropertyDetailsUiState.Error(result.error.toPropertyDetailsErrorMessage())
                }
            }
        }
    }

    private fun removeProperty(propertyId: String) {
        logger.log(PropertyDetailsLogEvent.RemovePropertyStarted)
        when (val result = watchlistRepository.removeProperty(propertyId)) {
            is RepositoryResult.Success -> {
                logger.log(PropertyDetailsLogEvent.RemovePropertySucceeded)
                updateSavedStatus(propertyId = propertyId, isSaved = false)
            }

            is RepositoryResult.Failure -> {
                logger.log(PropertyDetailsLogEvent.WatchlistActionFailed)
                if (result.error == RepositoryError.NotFound) {
                    updateSavedStatus(propertyId = propertyId, isSaved = false)
                } else {
                    state = PropertyDetailsUiState.Error(result.error.toPropertyDetailsErrorMessage())
                }
            }
        }
    }

    private fun addToComparison(propertyId: String) {
        logger.log(PropertyDetailsLogEvent.AddToComparison)
        updateComparisonStatus(propertyId = propertyId, isInComparison = true)
    }

    private fun removeFromComparison(propertyId: String) {
        logger.log(PropertyDetailsLogEvent.RemoveFromComparison)
        updateComparisonStatus(propertyId = propertyId, isInComparison = false)
    }

    private fun updateSavedStatus(propertyId: String, isSaved: Boolean) {
        val content = (state as? PropertyDetailsUiState.Content)?.content ?: return
        if (content.property.id != propertyId) return
        state = PropertyDetailsUiState.Content(
            content = content.copy(
                property = content.property.copy(isSaved = isSaved),
            ),
        )
    }

    private fun updateComparisonStatus(propertyId: String, isInComparison: Boolean) {
        val content = (state as? PropertyDetailsUiState.Content)?.content ?: return
        if (content.property.id != propertyId) return
        state = PropertyDetailsUiState.Content(
            content = content.copy(
                property = content.property.copy(isInComparison = isInComparison),
            ),
        )
    }
}

sealed interface PropertyDetailsUiState {
    data object Loading : PropertyDetailsUiState

    data object Empty : PropertyDetailsUiState

    data class Content(
        val content: PropertyDetailsContent,
    ) : PropertyDetailsUiState

    data class Error(
        val message: String,
    ) : PropertyDetailsUiState
}

@Immutable
data class PropertyDetailsContent(
    val property: PropertyDetailsUi,
)

@Immutable
data class PropertyDetailsUi(
    val id: String,
    val title: String,
    val areaId: String,
    val priceAmount: Double,
    val currency: String,
    val sizeSqft: Double,
    val expectedAnnualRentAmount: Double?,
    val annualCostsAmount: Double?,
    val propertyTypeLabel: String,
    val completionStatusLabel: String,
    val riskLabel: String?,
    val demandLabel: String?,
    val investmentScore: Int?,
    val grossRentalYieldPercentage: Double?,
    val netRentalYieldPercentage: Double?,
    val monthlyRentAmount: Double?,
    val monthlyCostsAmount: Double?,
    val monthlyCashFlowAmount: Double?,
    val annualCashFlowAmount: Double?,
    val pricePerSqftAmount: Double?,
    val simpleRoiPercentage: Double?,
    val isSaved: Boolean,
    val isInComparison: Boolean,
)

sealed interface PropertyDetailsUiEvent {
    data class LoadProperty(val propertyId: String) : PropertyDetailsUiEvent
    data object RetryClicked : PropertyDetailsUiEvent
    data class SavePropertyClicked(val propertyId: String) : PropertyDetailsUiEvent
    data class RemovePropertyClicked(val propertyId: String) : PropertyDetailsUiEvent
    data class AddToComparisonClicked(val propertyId: String) : PropertyDetailsUiEvent
    data class RemoveFromComparisonClicked(val propertyId: String) : PropertyDetailsUiEvent
    data class AreaClicked(val areaId: String) : PropertyDetailsUiEvent
    data object ComparisonClicked : PropertyDetailsUiEvent
}

sealed interface PropertyDetailsUiEffect {
    data class NavigateToAreaDetails(val areaId: String) : PropertyDetailsUiEffect
    data object NavigateToComparison : PropertyDetailsUiEffect
}

interface PropertyDetailsLogger {
    fun log(event: PropertyDetailsLogEvent)
}

enum class PropertyDetailsLogEvent {
    LoadStarted,
    LoadSucceeded,
    LoadFailed,
    RetryStarted,
    SavePropertyStarted,
    SavePropertySucceeded,
    RemovePropertyStarted,
    RemovePropertySucceeded,
    WatchlistActionFailed,
    AddToComparison,
    RemoveFromComparison,
    AreaSelected,
    ComparisonSelected,
}

object NoOpPropertyDetailsLogger : PropertyDetailsLogger {
    override fun log(event: PropertyDetailsLogEvent) = Unit
}

private fun PropertyDetails.toPropertyDetailsContent(): PropertyDetailsContent {
    return PropertyDetailsContent(
        property = property.toPropertyDetailsUi(isSaved = isSaved),
    )
}

private fun Property.toPropertyDetailsUi(isSaved: Boolean): PropertyDetailsUi {
    return PropertyDetailsUi(
        id = id,
        title = title,
        areaId = areaId,
        priceAmount = price.amount,
        currency = price.currency,
        sizeSqft = sizeSqft,
        expectedAnnualRentAmount = expectedAnnualRent?.amount,
        annualCostsAmount = annualCosts?.amount,
        propertyTypeLabel = propertyType.label(),
        completionStatusLabel = completionStatus.label(),
        riskLabel = riskLevel?.label(),
        demandLabel = demandLevel?.label(),
        investmentScore = investmentScore?.value,
        grossRentalYieldPercentage = investmentMetrics?.grossRentalYield?.percentage,
        netRentalYieldPercentage = investmentMetrics?.netRentalYield?.percentage,
        monthlyRentAmount = investmentMetrics?.monthlyRent?.amount,
        monthlyCostsAmount = investmentMetrics?.monthlyCosts?.amount,
        monthlyCashFlowAmount = investmentMetrics?.monthlyCashFlow?.amount,
        annualCashFlowAmount = investmentMetrics?.annualCashFlow?.amount,
        pricePerSqftAmount = investmentMetrics?.pricePerSqft?.amount,
        simpleRoiPercentage = investmentMetrics?.simpleRoiPercentage,
        isSaved = isSaved,
        isInComparison = false,
    )
}

private fun PropertyType.label(): String = when (this) {
    PropertyType.Apartment -> "Apartment"
    PropertyType.Villa -> "Villa"
    PropertyType.Townhouse -> "Townhouse"
    PropertyType.Studio -> "Studio"
    PropertyType.Penthouse -> "Penthouse"
    PropertyType.Commercial -> "Commercial"
}

private fun CompletionStatus.label(): String = when (this) {
    CompletionStatus.Ready -> "Ready"
    CompletionStatus.OffPlan -> "Off-plan"
    CompletionStatus.UnderConstruction -> "Under construction"
}

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

private fun RepositoryError.toPropertyDetailsErrorMessage(): String = when (this) {
    RepositoryError.NotFound -> "Property data is not available yet."
    RepositoryError.AlreadyExists -> "This property is already saved."
    RepositoryError.Unknown -> "Unable to load property details. Please try again."
}
