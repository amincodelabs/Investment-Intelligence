package amin.codelabs.realestateinvestmentintelligence.dashboard.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.CompletionStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentMetrics
import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentScore
import amin.codelabs.realestateinvestmentintelligence.domain.model.Money
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.model.PropertyType
import amin.codelabs.realestateinvestmentintelligence.domain.model.RentalYield
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetDashboardOverviewUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DashboardViewModelTest {
    @Test
    fun `dashboard view model starts loading`() {
        val viewModel = createViewModel()

        assertEquals(DashboardUiState.Loading, viewModel.state)
        assertNull(viewModel.effect)
    }

    @Test
    fun `load dashboard returns content sorted by investment quality`() {
        val logger = RecordingDashboardLogger()
        val viewModel = createViewModel(
            areas = listOf(
                area(id = "jvc", score = 82, yield = 7.1),
                area(id = "marina", score = 91, yield = 6.4),
            ),
            properties = listOf(
                property(id = "p-low", netYield = 5.2, score = 86),
                property(id = "p-high", netYield = 7.4, score = 80),
            ),
            savedAreas = listOf(area(id = "jvc")),
            savedProperties = listOf(property(id = "p-high")),
            logger = logger,
        )

        viewModel.onEvent(DashboardUiEvent.LoadDashboard)

        val state = assertIs<DashboardUiState.Content>(viewModel.state)
        assertEquals(2, state.content.marketOverview.areaCount)
        assertEquals(2, state.content.marketOverview.propertyCount)
        assertEquals("AED", state.content.marketOverview.currency)
        assertEquals("marina", state.content.topInvestmentAreas.first().id)
        assertEquals("p-high", state.content.topPropertyOpportunities.first().id)
        assertEquals(1, state.content.watchlistSummary.savedAreaCount)
        assertEquals(1, state.content.watchlistSummary.savedPropertyCount)
        assertTrue(DashboardLogEvent.LoadStarted in logger.events)
        assertTrue(DashboardLogEvent.LoadedContent in logger.events)
    }

    @Test
    fun `load dashboard returns empty state when repositories are empty`() {
        val logger = RecordingDashboardLogger()
        val viewModel = createViewModel(logger = logger)

        viewModel.onEvent(DashboardUiEvent.LoadDashboard)

        assertEquals(DashboardUiState.Empty, viewModel.state)
        assertTrue(DashboardLogEvent.LoadedEmpty in logger.events)
    }

    @Test
    fun `load dashboard returns error state when repository fails`() {
        val logger = RecordingDashboardLogger()
        val areaRepository = FakeAreaRepository(
            allAreasResult = RepositoryResult.Failure(RepositoryError.Unknown),
        )
        val viewModel = createViewModel(
            areaRepository = areaRepository,
            logger = logger,
        )

        viewModel.onEvent(DashboardUiEvent.LoadDashboard)

        val state = assertIs<DashboardUiState.Error>(viewModel.state)
        assertEquals("Unable to load dashboard data. Please try again.", state.message)
        assertTrue(DashboardLogEvent.LoadFailed in logger.events)
    }

    @Test
    fun `retry reloads dashboard after error`() {
        val areaRepository = FakeAreaRepository(
            allAreasResult = RepositoryResult.Failure(RepositoryError.Unknown),
        )
        val viewModel = createViewModel(
            areaRepository = areaRepository,
            properties = listOf(property(id = "p-1")),
        )

        viewModel.onEvent(DashboardUiEvent.LoadDashboard)
        areaRepository.allAreasResult = RepositoryResult.Success(listOf(area(id = "jvc")))
        viewModel.onEvent(DashboardUiEvent.RetryClicked)

        val state = assertIs<DashboardUiState.Content>(viewModel.state)
        assertEquals("jvc", state.content.topInvestmentAreas.single().id)
    }

    @Test
    fun `area click emits area navigation effect`() {
        val viewModel = createViewModel()

        viewModel.onEvent(DashboardUiEvent.AreaClicked("downtown"))

        assertEquals(DashboardUiEffect.NavigateToAreaDetails("downtown"), viewModel.effect)
    }

    @Test
    fun `property click emits property navigation effect`() {
        val viewModel = createViewModel()

        viewModel.onEvent(DashboardUiEvent.PropertyClicked("property-1"))

        assertEquals(DashboardUiEffect.NavigateToPropertyDetails("property-1"), viewModel.effect)
    }

    @Test
    fun `watchlist click emits watchlist navigation effect`() {
        val viewModel = createViewModel()

        viewModel.onEvent(DashboardUiEvent.WatchlistClicked)

        assertEquals(DashboardUiEffect.NavigateToWatchlist, viewModel.effect)
    }

    private fun createViewModel(
        areas: List<Area> = emptyList(),
        properties: List<Property> = emptyList(),
        savedAreas: List<Area> = emptyList(),
        savedProperties: List<Property> = emptyList(),
        areaRepository: FakeAreaRepository = FakeAreaRepository(RepositoryResult.Success(areas)),
        propertyRepository: FakePropertyRepository = FakePropertyRepository(RepositoryResult.Success(properties)),
        watchlistRepository: FakeWatchlistRepository = FakeWatchlistRepository(
            savedAreas = savedAreas,
            savedProperties = savedProperties,
        ),
        logger: DashboardLogger = NoOpDashboardLogger,
    ): DashboardViewModel = DefaultDashboardViewModel(
        getDashboardOverviewUseCase = GetDashboardOverviewUseCase(
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        ),
        logger = logger,
    )

    private fun area(
        id: String,
        score: Int = 80,
        yield: Double = 6.5,
    ): Area = Area(
        id = id,
        name = id.uppercase(),
        averagePropertyPrice = Money(1_200_000.0),
        averageRentalYield = RentalYield(yield),
        demandLevel = DemandLevel.High,
        riskLevel = RiskLevel.Medium,
        investmentScore = InvestmentScore(score),
    )

    private fun property(
        id: String,
        netYield: Double = 6.0,
        score: Int = 80,
    ): Property = Property(
        id = id,
        title = id.uppercase(),
        areaId = "jvc",
        price = Money(1_100_000.0),
        sizeSqft = 780.0,
        expectedAnnualRent = Money(82_000.0),
        annualCosts = Money(16_000.0),
        propertyType = PropertyType.Apartment,
        completionStatus = CompletionStatus.Ready,
        riskLevel = RiskLevel.Medium,
        demandLevel = DemandLevel.High,
        investmentScore = InvestmentScore(score),
        investmentMetrics = InvestmentMetrics(
            grossRentalYield = RentalYield(netYield + 1.0),
            netRentalYield = RentalYield(netYield),
            monthlyRent = Money(6_833.33),
            monthlyCosts = Money(1_333.33),
            monthlyCashFlow = Money(5_500.0),
            annualCashFlow = Money(66_000.0),
            pricePerSqft = Money(1_410.25),
            simpleRoiPercentage = netYield,
        ),
    )

    private class RecordingDashboardLogger : DashboardLogger {
        val events = mutableListOf<DashboardLogEvent>()

        override fun log(event: DashboardLogEvent) {
            events += event
        }
    }

    private class FakeAreaRepository(
        var allAreasResult: RepositoryResult<List<Area>>,
    ) : AreaRepository {
        override fun getAllAreas(): RepositoryResult<List<Area>> = allAreasResult

        override fun getAreaById(id: String): RepositoryResult<Area> {
            val areas = (allAreasResult as? RepositoryResult.Success)?.value.orEmpty()
            return areas.firstOrNull { it.id == id }?.let { RepositoryResult.Success(it) }
                ?: RepositoryResult.Failure(RepositoryError.NotFound)
        }
    }

    private class FakePropertyRepository(
        var allPropertiesResult: RepositoryResult<List<Property>>,
    ) : PropertyRepository {
        override fun getAllProperties(): RepositoryResult<List<Property>> = allPropertiesResult

        override fun getPropertyById(id: String): RepositoryResult<Property> {
            val properties = (allPropertiesResult as? RepositoryResult.Success)?.value.orEmpty()
            return properties.firstOrNull { it.id == id }?.let { RepositoryResult.Success(it) }
                ?: RepositoryResult.Failure(RepositoryError.NotFound)
        }

        override fun getPropertiesByAreaId(areaId: String): RepositoryResult<List<Property>> {
            val properties = (allPropertiesResult as? RepositoryResult.Success)?.value.orEmpty()
                .filter { it.areaId == areaId }
            return RepositoryResult.Success(properties)
        }
    }

    private class FakeWatchlistRepository(
        private val savedAreas: List<Area>,
        private val savedProperties: List<Property>,
        private val savedAreasResult: RepositoryResult<List<Area>> = RepositoryResult.Success(savedAreas),
        private val savedPropertiesResult: RepositoryResult<List<Property>> = RepositoryResult.Success(savedProperties),
    ) : WatchlistRepository {
        override fun getSavedProperties(): RepositoryResult<List<Property>> = savedPropertiesResult

        override fun getSavedAreas(): RepositoryResult<List<Area>> = savedAreasResult

        override fun saveProperty(propertyId: String): RepositoryResult<Unit> = RepositoryResult.Success(Unit)

        override fun removeProperty(propertyId: String): RepositoryResult<Unit> = RepositoryResult.Success(Unit)

        override fun saveArea(areaId: String): RepositoryResult<Unit> = RepositoryResult.Success(Unit)

        override fun removeArea(areaId: String): RepositoryResult<Unit> = RepositoryResult.Success(Unit)
    }
}
