package amin.codelabs.realestateinvestmentintelligence.area.presentation

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
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaDetailsUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaListUseCase
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetAreaPropertiesUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AreaIntelligenceViewModelTest {
    @Test
    fun `area intelligence view model starts loading`() {
        val viewModel = createViewModel()

        assertEquals(AreaIntelligenceUiState.Loading, viewModel.state)
        assertNull(viewModel.effect)
    }

    @Test
    fun `load areas returns content with saved status`() {
        val logger = RecordingAreaIntelligenceLogger()
        val viewModel = createViewModel(
            areas = listOf(area("jvc"), area("marina")),
            savedAreas = listOf(area("marina")),
            logger = logger,
        )

        viewModel.onEvent(AreaIntelligenceUiEvent.LoadAreas)

        val state = assertIs<AreaIntelligenceUiState.Content>(viewModel.state)
        assertEquals(2, state.content.areas.size)
        assertFalse(state.content.areas.first { it.id == "jvc" }.isSaved)
        assertTrue(state.content.areas.first { it.id == "marina" }.isSaved)
        assertTrue(AreaIntelligenceLogEvent.LoadAreasStarted in logger.events)
        assertTrue(AreaIntelligenceLogEvent.LoadAreasSucceeded in logger.events)
    }

    @Test
    fun `load areas returns empty state when no areas exist`() {
        val logger = RecordingAreaIntelligenceLogger()
        val viewModel = createViewModel(logger = logger)

        viewModel.onEvent(AreaIntelligenceUiEvent.LoadAreas)

        assertEquals(AreaIntelligenceUiState.Empty, viewModel.state)
        assertTrue(AreaIntelligenceLogEvent.LoadedEmpty in logger.events)
    }

    @Test
    fun `load area details returns selected area and related properties`() {
        val viewModel = createViewModel(
            areas = listOf(area("jvc")),
            properties = listOf(property("property-1", areaId = "jvc")),
            savedAreas = listOf(area("jvc")),
        )

        viewModel.onEvent(AreaIntelligenceUiEvent.LoadAreaDetails("jvc"))

        val state = assertIs<AreaIntelligenceUiState.Content>(viewModel.state)
        assertEquals("jvc", state.content.selectedArea?.id)
        assertEquals(true, state.content.selectedArea?.isSaved)
        assertEquals("property-1", state.content.properties.single().id)
    }

    @Test
    fun `load properties by area returns related properties`() {
        val viewModel = createViewModel(
            properties = listOf(
                property("property-1", areaId = "jvc"),
                property("property-2", areaId = "marina"),
            ),
        )

        viewModel.onEvent(AreaIntelligenceUiEvent.LoadPropertiesByArea("jvc"))

        val state = assertIs<AreaIntelligenceUiState.Content>(viewModel.state)
        assertEquals(listOf("property-1"), state.content.properties.map { it.id })
    }

    @Test
    fun `load areas returns error state when repository fails`() {
        val viewModel = createViewModel(
            areaRepository = FakeAreaRepository(
                allAreasResult = RepositoryResult.Failure(RepositoryError.Unknown),
            ),
        )

        viewModel.onEvent(AreaIntelligenceUiEvent.LoadAreas)

        val state = assertIs<AreaIntelligenceUiState.Error>(viewModel.state)
        assertEquals("Unable to load area intelligence. Please try again.", state.message)
    }

    @Test
    fun `retry repeats last failed area load`() {
        val areaRepository = FakeAreaRepository(
            allAreasResult = RepositoryResult.Failure(RepositoryError.Unknown),
        )
        val viewModel = createViewModel(areaRepository = areaRepository)

        viewModel.onEvent(AreaIntelligenceUiEvent.LoadAreas)
        areaRepository.allAreasResult = RepositoryResult.Success(listOf(area("jvc")))
        viewModel.onEvent(AreaIntelligenceUiEvent.RetryClicked)

        val state = assertIs<AreaIntelligenceUiState.Content>(viewModel.state)
        assertEquals("jvc", state.content.areas.single().id)
    }

    @Test
    fun `save and remove area updates watchlist state`() {
        val watchlistRepository = FakeWatchlistRepository(savedAreas = emptyList())
        val viewModel = createViewModel(
            areas = listOf(area("jvc")),
            watchlistRepository = watchlistRepository,
        )

        viewModel.onEvent(AreaIntelligenceUiEvent.LoadAreas)
        viewModel.onEvent(AreaIntelligenceUiEvent.SaveAreaClicked("jvc"))

        val savedState = assertIs<AreaIntelligenceUiState.Content>(viewModel.state)
        assertTrue(savedState.content.areas.single().isSaved)
        assertTrue("jvc" in watchlistRepository.savedAreaIds)

        viewModel.onEvent(AreaIntelligenceUiEvent.RemoveAreaClicked("jvc"))

        val removedState = assertIs<AreaIntelligenceUiState.Content>(viewModel.state)
        assertFalse(removedState.content.areas.single().isSaved)
        assertFalse("jvc" in watchlistRepository.savedAreaIds)
    }

    @Test
    fun `area click emits area navigation effect`() {
        val viewModel = createViewModel()

        viewModel.onEvent(AreaIntelligenceUiEvent.AreaClicked("jvc"))

        assertEquals(AreaIntelligenceUiEffect.NavigateToAreaDetails("jvc"), viewModel.effect)
    }

    @Test
    fun `property click emits property navigation effect`() {
        val viewModel = createViewModel()

        viewModel.onEvent(AreaIntelligenceUiEvent.PropertyClicked("property-1"))

        assertEquals(AreaIntelligenceUiEffect.NavigateToPropertyDetails("property-1"), viewModel.effect)
    }

    private fun createViewModel(
        areas: List<Area> = emptyList(),
        properties: List<Property> = emptyList(),
        savedAreas: List<Area> = emptyList(),
        areaRepository: FakeAreaRepository = FakeAreaRepository(RepositoryResult.Success(areas)),
        propertyRepository: FakePropertyRepository = FakePropertyRepository(RepositoryResult.Success(properties)),
        watchlistRepository: FakeWatchlistRepository = FakeWatchlistRepository(savedAreas = savedAreas),
        logger: AreaIntelligenceLogger = NoOpAreaIntelligenceLogger,
    ): AreaIntelligenceViewModel = DefaultAreaIntelligenceViewModel(
        getAreaListUseCase = GetAreaListUseCase(
            areaRepository = areaRepository,
            watchlistRepository = watchlistRepository,
        ),
        getAreaDetailsUseCase = GetAreaDetailsUseCase(
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        ),
        getAreaPropertiesUseCase = GetAreaPropertiesUseCase(
            propertyRepository = propertyRepository,
        ),
        watchlistRepository = watchlistRepository,
        logger = logger,
    )

    private fun area(
        id: String,
        score: Int = 82,
        yield: Double = 7.1,
    ): Area = Area(
        id = id,
        name = id.uppercase(),
        averagePropertyPrice = Money(1_200_000.0),
        averageRentalYield = RentalYield(yield),
        demandLevel = DemandLevel.High,
        riskLevel = RiskLevel.Medium,
        appreciationPotentialPercentage = 4.8,
        investmentScore = InvestmentScore(score),
    )

    private fun property(
        id: String,
        areaId: String,
        netYield: Double = 6.2,
    ): Property = Property(
        id = id,
        title = id.uppercase(),
        areaId = areaId,
        price = Money(1_100_000.0),
        sizeSqft = 780.0,
        expectedAnnualRent = Money(82_000.0),
        annualCosts = Money(16_000.0),
        propertyType = PropertyType.Apartment,
        completionStatus = CompletionStatus.Ready,
        riskLevel = RiskLevel.Medium,
        demandLevel = DemandLevel.High,
        investmentScore = InvestmentScore(83),
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

    private class RecordingAreaIntelligenceLogger : AreaIntelligenceLogger {
        val events = mutableListOf<AreaIntelligenceLogEvent>()

        override fun log(event: AreaIntelligenceLogEvent) {
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
        private val allPropertiesResult: RepositoryResult<List<Property>>,
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
        savedAreas: List<Area>,
    ) : WatchlistRepository {
        val savedAreaIds = savedAreas.map { it.id }.toMutableSet()

        override fun getSavedProperties(): RepositoryResult<List<Property>> = RepositoryResult.Success(emptyList())

        override fun getSavedAreas(): RepositoryResult<List<Area>> {
            return RepositoryResult.Success(savedAreaIds.map { area(id = it) })
        }

        override fun saveProperty(propertyId: String): RepositoryResult<Unit> = RepositoryResult.Success(Unit)

        override fun removeProperty(propertyId: String): RepositoryResult<Unit> = RepositoryResult.Success(Unit)

        override fun saveArea(areaId: String): RepositoryResult<Unit> {
            if (!savedAreaIds.add(areaId)) {
                return RepositoryResult.Failure(RepositoryError.AlreadyExists)
            }
            return RepositoryResult.Success(Unit)
        }

        override fun removeArea(areaId: String): RepositoryResult<Unit> {
            if (!savedAreaIds.remove(areaId)) {
                return RepositoryResult.Failure(RepositoryError.NotFound)
            }
            return RepositoryResult.Success(Unit)
        }

        private fun area(id: String): Area = Area(id = id, name = id.uppercase())
    }
}
