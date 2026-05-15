package amin.codelabs.realestateinvestmentintelligence.domain.usecase

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository

class GetDashboardOverviewUseCase(
    private val areaRepository: AreaRepository,
    private val propertyRepository: PropertyRepository,
    private val watchlistRepository: WatchlistRepository,
) {
    fun execute(): DashboardOverviewResult {
        val areas = when (val result = areaRepository.getAllAreas()) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> return DashboardOverviewResult.Failure(result.error)
        }
        val properties = when (val result = propertyRepository.getAllProperties()) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> return DashboardOverviewResult.Failure(result.error)
        }
        val savedAreas = when (val result = watchlistRepository.getSavedAreas()) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> return DashboardOverviewResult.Failure(result.error)
        }
        val savedProperties = when (val result = watchlistRepository.getSavedProperties()) {
            is RepositoryResult.Success -> result.value
            is RepositoryResult.Failure -> return DashboardOverviewResult.Failure(result.error)
        }

        return DashboardOverviewResult.Success(
            DashboardOverview(
                marketOverview = DashboardMarketOverview(
                    areaCount = areas.size,
                    propertyCount = properties.size,
                    averageRentalYieldPercentage = areas.mapNotNull { it.averageRentalYield?.percentage }
                        .takeIf { it.isNotEmpty() }
                        ?.average(),
                    averageInvestmentScore = areas.mapNotNull { it.investmentScore?.value }
                        .takeIf { it.isNotEmpty() }
                        ?.average(),
                    currency = properties.firstOrNull()?.price?.currency
                        ?: areas.firstNotNullOfOrNull { it.averagePropertyPrice?.currency }
                        ?: DEFAULT_CURRENCY,
                ),
                topInvestmentAreas = areas
                    .sortedWith(
                        compareByDescending<Area> { it.investmentScore?.value ?: Int.MIN_VALUE }
                            .thenByDescending { it.averageRentalYield?.percentage ?: Double.NEGATIVE_INFINITY },
                    )
                    .take(MAX_TOP_ITEMS),
                topPropertyOpportunities = properties
                    .sortedWith(
                        compareByDescending<Property> {
                            it.investmentMetrics?.netRentalYield?.percentage
                                ?: it.investmentMetrics?.grossRentalYield?.percentage
                                ?: Double.NEGATIVE_INFINITY
                        }.thenByDescending { it.investmentScore?.value ?: Int.MIN_VALUE },
                    )
                    .take(MAX_TOP_ITEMS),
                watchlistSummary = DashboardWatchlistSummary(
                    savedAreaCount = savedAreas.size,
                    savedPropertyCount = savedProperties.size,
                ),
            ),
        )
    }

    private companion object {
        const val MAX_TOP_ITEMS = 3
        const val DEFAULT_CURRENCY = "AED"
    }
}

data class DashboardOverview(
    val marketOverview: DashboardMarketOverview,
    val topInvestmentAreas: List<Area>,
    val topPropertyOpportunities: List<Property>,
    val watchlistSummary: DashboardWatchlistSummary,
) {
    val isEmpty: Boolean = marketOverview.areaCount == 0 &&
        marketOverview.propertyCount == 0 &&
        topInvestmentAreas.isEmpty() &&
        topPropertyOpportunities.isEmpty()
}

data class DashboardMarketOverview(
    val areaCount: Int,
    val propertyCount: Int,
    val averageRentalYieldPercentage: Double?,
    val averageInvestmentScore: Double?,
    val currency: String,
)

data class DashboardWatchlistSummary(
    val savedAreaCount: Int,
    val savedPropertyCount: Int,
)

sealed interface DashboardOverviewResult {
    data class Success(
        val overview: DashboardOverview,
    ) : DashboardOverviewResult

    data class Failure(
        val error: RepositoryError,
    ) : DashboardOverviewResult
}
