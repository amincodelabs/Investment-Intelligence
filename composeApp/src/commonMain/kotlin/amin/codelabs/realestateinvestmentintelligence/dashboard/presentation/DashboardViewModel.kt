package amin.codelabs.realestateinvestmentintelligence.dashboard.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.DashboardOverview
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.DashboardOverviewResult
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetDashboardOverviewUseCase
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface DashboardViewModel {
    val state: DashboardUiState
    val effect: DashboardUiEffect?

    fun onEvent(event: DashboardUiEvent)
    fun consumeEffect()
}

class DefaultDashboardViewModel(
    private val getDashboardOverviewUseCase: GetDashboardOverviewUseCase,
    private val logger: DashboardLogger = NoOpDashboardLogger,
) : DashboardViewModel {
    override var state by mutableStateOf<DashboardUiState>(DashboardUiState.Loading)
        private set

    override var effect by mutableStateOf<DashboardUiEffect?>(null)
        private set

    override fun onEvent(event: DashboardUiEvent) {
        when (event) {
            DashboardUiEvent.LoadDashboard,
            DashboardUiEvent.RetryClicked,
            -> loadDashboard(isRetry = event == DashboardUiEvent.RetryClicked)

            is DashboardUiEvent.AreaClicked -> {
                logger.log(DashboardLogEvent.AreaSelected)
                effect = DashboardUiEffect.NavigateToAreaDetails(event.areaId)
            }

            is DashboardUiEvent.PropertyClicked -> {
                logger.log(DashboardLogEvent.PropertySelected)
                effect = DashboardUiEffect.NavigateToPropertyDetails(event.propertyId)
            }

            DashboardUiEvent.WatchlistClicked -> {
                logger.log(DashboardLogEvent.WatchlistSelected)
                effect = DashboardUiEffect.NavigateToWatchlist
            }
        }
    }

    override fun consumeEffect() {
        effect = null
    }

    private fun loadDashboard(isRetry: Boolean) {
        logger.log(if (isRetry) DashboardLogEvent.RetryStarted else DashboardLogEvent.LoadStarted)
        state = DashboardUiState.Loading

        when (val result = getDashboardOverviewUseCase.execute()) {
            is DashboardOverviewResult.Success -> {
                state = if (result.overview.isEmpty) {
                    logger.log(DashboardLogEvent.LoadedEmpty)
                    DashboardUiState.Empty
                } else {
                    logger.log(DashboardLogEvent.LoadedContent)
                    DashboardUiState.Content(result.overview.toUiContent())
                }
            }

            is DashboardOverviewResult.Failure -> {
                logger.log(DashboardLogEvent.LoadFailed)
                state = DashboardUiState.Error(
                    message = result.error.toDashboardErrorMessage(),
                )
            }
        }
    }
}

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data object Empty : DashboardUiState

    data class Content(
        val content: DashboardContent,
    ) : DashboardUiState

    data class Error(
        val message: String,
    ) : DashboardUiState
}

@Immutable
data class DashboardContent(
    val marketOverview: DashboardMarketOverviewUi,
    val topInvestmentAreas: List<DashboardAreaItemUi>,
    val topPropertyOpportunities: List<DashboardPropertyItemUi>,
    val watchlistSummary: DashboardWatchlistSummaryUi,
)

@Immutable
data class DashboardMarketOverviewUi(
    val areaCount: Int,
    val propertyCount: Int,
    val averageRentalYieldPercentage: Double?,
    val averageInvestmentScore: Double?,
    val currency: String,
)

@Immutable
data class DashboardAreaItemUi(
    val id: String,
    val name: String,
    val averageRentalYieldPercentage: Double?,
    val investmentScore: Int?,
    val demandLabel: String?,
    val riskLabel: String?,
)

@Immutable
data class DashboardPropertyItemUi(
    val id: String,
    val title: String,
    val areaId: String,
    val priceAmount: Double,
    val currency: String,
    val netRentalYieldPercentage: Double?,
    val grossRentalYieldPercentage: Double?,
    val investmentScore: Int?,
)

@Immutable
data class DashboardWatchlistSummaryUi(
    val savedAreaCount: Int,
    val savedPropertyCount: Int,
)

sealed interface DashboardUiEvent {
    data object LoadDashboard : DashboardUiEvent
    data object RetryClicked : DashboardUiEvent
    data class AreaClicked(val areaId: String) : DashboardUiEvent
    data class PropertyClicked(val propertyId: String) : DashboardUiEvent
    data object WatchlistClicked : DashboardUiEvent
}

sealed interface DashboardUiEffect {
    data class NavigateToAreaDetails(val areaId: String) : DashboardUiEffect
    data class NavigateToPropertyDetails(val propertyId: String) : DashboardUiEffect
    data object NavigateToWatchlist : DashboardUiEffect
}

interface DashboardLogger {
    fun log(event: DashboardLogEvent)
}

enum class DashboardLogEvent {
    LoadStarted,
    RetryStarted,
    LoadedContent,
    LoadedEmpty,
    LoadFailed,
    AreaSelected,
    PropertySelected,
    WatchlistSelected,
}

object NoOpDashboardLogger : DashboardLogger {
    override fun log(event: DashboardLogEvent) = Unit
}

private fun DashboardOverview.toUiContent(): DashboardContent = DashboardContent(
    marketOverview = DashboardMarketOverviewUi(
        areaCount = marketOverview.areaCount,
        propertyCount = marketOverview.propertyCount,
        averageRentalYieldPercentage = marketOverview.averageRentalYieldPercentage,
        averageInvestmentScore = marketOverview.averageInvestmentScore,
        currency = marketOverview.currency,
    ),
    topInvestmentAreas = topInvestmentAreas.map { it.toDashboardAreaItemUi() },
    topPropertyOpportunities = topPropertyOpportunities.map { it.toDashboardPropertyItemUi() },
    watchlistSummary = DashboardWatchlistSummaryUi(
        savedAreaCount = watchlistSummary.savedAreaCount,
        savedPropertyCount = watchlistSummary.savedPropertyCount,
    ),
)

private fun Area.toDashboardAreaItemUi(): DashboardAreaItemUi = DashboardAreaItemUi(
    id = id,
    name = name,
    averageRentalYieldPercentage = averageRentalYield?.percentage,
    investmentScore = investmentScore?.value,
    demandLabel = demandLevel?.label(),
    riskLabel = riskLevel?.label(),
)

private fun Property.toDashboardPropertyItemUi(): DashboardPropertyItemUi = DashboardPropertyItemUi(
    id = id,
    title = title,
    areaId = areaId,
    priceAmount = price.amount,
    currency = price.currency,
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

private fun RepositoryError.toDashboardErrorMessage(): String = when (this) {
    RepositoryError.NotFound -> "Dashboard data is not available yet."
    RepositoryError.AlreadyExists -> "Dashboard data could not be refreshed."
    RepositoryError.Unknown -> "Unable to load dashboard data. Please try again."
}
