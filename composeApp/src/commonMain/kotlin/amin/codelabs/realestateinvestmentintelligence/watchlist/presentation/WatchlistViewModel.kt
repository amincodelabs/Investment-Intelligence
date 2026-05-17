package amin.codelabs.realestateinvestmentintelligence.watchlist.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface WatchlistViewModel {
    val state: WatchlistUiState
    val effect: WatchlistUiEffect?

    fun onEvent(event: WatchlistUiEvent)
    fun consumeEffect()
}

class DefaultWatchlistViewModel(
    private val watchlistRepository: WatchlistRepository,
    private val logger: WatchlistLogger = NoOpWatchlistLogger,
) : WatchlistViewModel {
    override var state by mutableStateOf<WatchlistUiState>(WatchlistUiState.Loading)
        private set

    override var effect by mutableStateOf<WatchlistUiEffect?>(null)
        private set

    override fun onEvent(event: WatchlistUiEvent) {
        when (event) {
            WatchlistUiEvent.LoadWatchlist -> loadWatchlist(isRetry = false)
            WatchlistUiEvent.RetryClicked -> loadWatchlist(isRetry = true)
            is WatchlistUiEvent.RemovePropertyClicked -> removeProperty(event.propertyId)
            is WatchlistUiEvent.RemoveAreaClicked -> removeArea(event.areaId)
            is WatchlistUiEvent.PropertyClicked -> {
                logger.log(WatchlistLogEvent.PropertySelected)
                effect = WatchlistUiEffect.NavigateToPropertyDetails(event.propertyId)
            }
            is WatchlistUiEvent.AreaClicked -> {
                logger.log(WatchlistLogEvent.AreaSelected)
                effect = WatchlistUiEffect.NavigateToAreaDetails(event.areaId)
            }
        }
    }

    override fun consumeEffect() {
        effect = null
    }

    private fun loadWatchlist(isRetry: Boolean) {
        logger.log(if (isRetry) WatchlistLogEvent.RetryStarted else WatchlistLogEvent.LoadStarted)
        state = WatchlistUiState.Loading

        val savedProperties = when (val result = watchlistRepository.getSavedProperties()) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> {
                logger.log(WatchlistLogEvent.LoadFailed)
                state = WatchlistUiState.Error(result.error.toMessage())
                return
            }
        }

        val savedAreas = when (val result = watchlistRepository.getSavedAreas()) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> {
                logger.log(WatchlistLogEvent.LoadFailed)
                state = WatchlistUiState.Error(result.error.toMessage())
                return
            }
        }

        if (savedProperties.isEmpty() && savedAreas.isEmpty()) {
            logger.log(WatchlistLogEvent.LoadedEmpty)
            state = WatchlistUiState.Empty
        } else {
            logger.log(WatchlistLogEvent.LoadedContent)
            state = WatchlistUiState.Content(
                content = WatchlistContent(
                    savedProperties = savedProperties.map { it.toPropertyUi() },
                    savedAreas = savedAreas.map { it.toAreaUi() },
                ),
            )
        }
    }

    private fun removeProperty(propertyId: String) {
        logger.log(WatchlistLogEvent.RemovePropertyStarted)
        when (val result = watchlistRepository.removeProperty(propertyId)) {
            is RepositoryResult.Success -> {
                logger.log(WatchlistLogEvent.RemovePropertySucceeded)
                updatePropertyRemoved(propertyId)
            }

            is RepositoryResult.Failure -> {
                logger.log(WatchlistLogEvent.WatchlistActionFailed)
                if (result.error == RepositoryError.NotFound) {
                    updatePropertyRemoved(propertyId)
                } else {
                    state = WatchlistUiState.Error(result.error.toMessage())
                }
            }
        }
    }

    private fun removeArea(areaId: String) {
        logger.log(WatchlistLogEvent.RemoveAreaStarted)
        when (val result = watchlistRepository.removeArea(areaId)) {
            is RepositoryResult.Success -> {
                logger.log(WatchlistLogEvent.RemoveAreaSucceeded)
                updateAreaRemoved(areaId)
            }

            is RepositoryResult.Failure -> {
                logger.log(WatchlistLogEvent.WatchlistActionFailed)
                if (result.error == RepositoryError.NotFound) {
                    updateAreaRemoved(areaId)
                } else {
                    state = WatchlistUiState.Error(result.error.toMessage())
                }
            }
        }
    }

    private fun updatePropertyRemoved(propertyId: String) {
        val content = (state as? WatchlistUiState.Content)?.content ?: return
        val updatedProperties = content.savedProperties.filterNot { it.id == propertyId }
        state = if (updatedProperties.isEmpty() && content.savedAreas.isEmpty()) {
            WatchlistUiState.Empty
        } else {
            WatchlistUiState.Content(content.copy(savedProperties = updatedProperties))
        }
    }

    private fun updateAreaRemoved(areaId: String) {
        val content = (state as? WatchlistUiState.Content)?.content ?: return
        val updatedAreas = content.savedAreas.filterNot { it.id == areaId }
        state = if (content.savedProperties.isEmpty() && updatedAreas.isEmpty()) {
            WatchlistUiState.Empty
        } else {
            WatchlistUiState.Content(content.copy(savedAreas = updatedAreas))
        }
    }
}

@Immutable
data class WatchlistContent(
    val savedProperties: List<WatchlistPropertyItemUi>,
    val savedAreas: List<WatchlistAreaItemUi>,
)

sealed interface WatchlistUiState {
    data object Loading : WatchlistUiState
    data object Empty : WatchlistUiState
    data class Content(val content: WatchlistContent) : WatchlistUiState
    data class Error(val message: String) : WatchlistUiState
}

@Immutable
data class WatchlistPropertyItemUi(
    val id: String,
    val title: String,
    val areaId: String,
    val priceAmount: Double,
    val currency: String,
    val netRentalYieldPercentage: Double?,
    val investmentScore: Int?,
)

@Immutable
data class WatchlistAreaItemUi(
    val id: String,
    val name: String,
    val averagePriceAmount: Double?,
    val currency: String,
    val averageRentalYieldPercentage: Double?,
    val demandLabel: String?,
    val riskLabel: String?,
    val investmentScore: Int?,
)

sealed interface WatchlistUiEvent {
    data object LoadWatchlist : WatchlistUiEvent
    data object RetryClicked : WatchlistUiEvent
    data class RemovePropertyClicked(val propertyId: String) : WatchlistUiEvent
    data class RemoveAreaClicked(val areaId: String) : WatchlistUiEvent
    data class PropertyClicked(val propertyId: String) : WatchlistUiEvent
    data class AreaClicked(val areaId: String) : WatchlistUiEvent
}

sealed interface WatchlistUiEffect {
    data class NavigateToPropertyDetails(val propertyId: String) : WatchlistUiEffect
    data class NavigateToAreaDetails(val areaId: String) : WatchlistUiEffect
}

interface WatchlistLogger {
    fun log(event: WatchlistLogEvent)
}

enum class WatchlistLogEvent {
    LoadStarted,
    RetryStarted,
    LoadedContent,
    LoadedEmpty,
    LoadFailed,
    RemovePropertyStarted,
    RemovePropertySucceeded,
    RemoveAreaStarted,
    RemoveAreaSucceeded,
    WatchlistActionFailed,
    PropertySelected,
    AreaSelected,
}

object NoOpWatchlistLogger : WatchlistLogger {
    override fun log(event: WatchlistLogEvent) = Unit
}

private fun Property.toPropertyUi(): WatchlistPropertyItemUi = WatchlistPropertyItemUi(
    id = id,
    title = title,
    areaId = areaId,
    priceAmount = price.amount,
    currency = price.currency,
    netRentalYieldPercentage = investmentMetrics?.netRentalYield?.percentage,
    investmentScore = investmentScore?.value,
)

private fun Area.toAreaUi(): WatchlistAreaItemUi = WatchlistAreaItemUi(
    id = id,
    name = name,
    averagePriceAmount = averagePropertyPrice?.amount,
    currency = averagePropertyPrice?.currency ?: "AED",
    averageRentalYieldPercentage = averageRentalYield?.percentage,
    demandLabel = demandLevel?.label(),
    riskLabel = riskLevel?.label(),
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

private fun RepositoryError.toMessage(): String = when (this) {
    RepositoryError.NotFound -> "Watchlist item is not available."
    RepositoryError.AlreadyExists -> "This item is already saved."
    RepositoryError.Unknown -> "Unable to load watchlist. Please try again."
}
