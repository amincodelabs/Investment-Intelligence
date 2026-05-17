package amin.codelabs.realestateinvestmentintelligence.property.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.CompletionStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentMetrics
import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentScore
import amin.codelabs.realestateinvestmentintelligence.domain.model.Money
import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.model.PropertyType
import amin.codelabs.realestateinvestmentintelligence.domain.model.RentalYield
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.domain.usecase.GetPropertyDetailsUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PropertyDetailsViewModelTest {
    @Test
    fun `property details view model starts loading`() {
        val viewModel = createViewModel()

        assertEquals(PropertyDetailsUiState.Loading, viewModel.state)
        assertNull(viewModel.effect)
    }

    @Test
    fun `load property returns content with investment metrics and saved status`() {
        val logger = RecordingPropertyDetailsLogger()
        val viewModel = createViewModel(
            properties = listOf(property(id = "property-1")),
            savedProperties = listOf(property(id = "property-1")),
            logger = logger,
        )

        viewModel.onEvent(PropertyDetailsUiEvent.LoadProperty("property-1"))

        val state = assertIs<PropertyDetailsUiState.Content>(viewModel.state)
        assertEquals("property-1", state.content.property.id)
        assertEquals("PROPERTY-1", state.content.property.title)
        assertEquals(1_100_000.0, state.content.property.priceAmount)
        assertEquals(6.2, state.content.property.netRentalYieldPercentage)
        assertEquals(true, state.content.property.isSaved)
        assertFalse(state.content.property.isInComparison)
        assertTrue(PropertyDetailsLogEvent.LoadStarted in logger.events)
        assertTrue(PropertyDetailsLogEvent.LoadSucceeded in logger.events)
    }

    @Test
    fun `load property returns error when property is not found`() {
        val logger = RecordingPropertyDetailsLogger()
        val viewModel = createViewModel(logger = logger)

        viewModel.onEvent(PropertyDetailsUiEvent.LoadProperty("missing"))

        val state = assertIs<PropertyDetailsUiState.Error>(viewModel.state)
        assertEquals("Property data is not available yet.", state.message)
        assertTrue(PropertyDetailsLogEvent.LoadFailed in logger.events)
    }

    @Test
    fun `blank property id returns empty state`() {
        val viewModel = createViewModel()

        viewModel.onEvent(PropertyDetailsUiEvent.LoadProperty(""))

        assertEquals(PropertyDetailsUiState.Empty, viewModel.state)
    }

    @Test
    fun `retry reloads last property after error`() {
        val propertyRepository = FakePropertyRepository(
            propertiesResult = RepositoryResult.Failure(RepositoryError.Unknown),
        )
        val viewModel = createViewModel(propertyRepository = propertyRepository)

        viewModel.onEvent(PropertyDetailsUiEvent.LoadProperty("property-1"))
        propertyRepository.propertiesResult = RepositoryResult.Success(listOf(property(id = "property-1")))
        viewModel.onEvent(PropertyDetailsUiEvent.RetryClicked)

        val state = assertIs<PropertyDetailsUiState.Content>(viewModel.state)
        assertEquals("property-1", state.content.property.id)
    }

    @Test
    fun `save and remove property updates watchlist state`() {
        val watchlistRepository = FakeWatchlistRepository(savedProperties = emptyList())
        val viewModel = createViewModel(
            properties = listOf(property(id = "property-1")),
            watchlistRepository = watchlistRepository,
        )

        viewModel.onEvent(PropertyDetailsUiEvent.LoadProperty("property-1"))
        viewModel.onEvent(PropertyDetailsUiEvent.SavePropertyClicked("property-1"))

        val savedState = assertIs<PropertyDetailsUiState.Content>(viewModel.state)
        assertTrue(savedState.content.property.isSaved)
        assertTrue("property-1" in watchlistRepository.savedPropertyIds)

        viewModel.onEvent(PropertyDetailsUiEvent.RemovePropertyClicked("property-1"))

        val removedState = assertIs<PropertyDetailsUiState.Content>(viewModel.state)
        assertFalse(removedState.content.property.isSaved)
        assertFalse("property-1" in watchlistRepository.savedPropertyIds)
    }

    @Test
    fun `add and remove comparison updates comparison state`() {
        val viewModel = createViewModel(
            properties = listOf(property(id = "property-1")),
        )

        viewModel.onEvent(PropertyDetailsUiEvent.LoadProperty("property-1"))
        viewModel.onEvent(PropertyDetailsUiEvent.AddToComparisonClicked("property-1"))

        val addedState = assertIs<PropertyDetailsUiState.Content>(viewModel.state)
        assertTrue(addedState.content.property.isInComparison)

        viewModel.onEvent(PropertyDetailsUiEvent.RemoveFromComparisonClicked("property-1"))

        val removedState = assertIs<PropertyDetailsUiState.Content>(viewModel.state)
        assertFalse(removedState.content.property.isInComparison)
    }

    @Test
    fun `area click emits area navigation effect`() {
        val viewModel = createViewModel()

        viewModel.onEvent(PropertyDetailsUiEvent.AreaClicked("area-jvc"))

        assertEquals(PropertyDetailsUiEffect.NavigateToAreaDetails("area-jvc"), viewModel.effect)
    }

    @Test
    fun `comparison click emits comparison navigation effect`() {
        val viewModel = createViewModel()

        viewModel.onEvent(PropertyDetailsUiEvent.ComparisonClicked)

        assertEquals(PropertyDetailsUiEffect.NavigateToComparison, viewModel.effect)
    }

    private fun createViewModel(
        properties: List<Property> = emptyList(),
        savedProperties: List<Property> = emptyList(),
        propertyRepository: FakePropertyRepository = FakePropertyRepository(
            RepositoryResult.Success(properties),
        ),
        watchlistRepository: FakeWatchlistRepository = FakeWatchlistRepository(
            savedProperties = savedProperties,
        ),
        logger: PropertyDetailsLogger = NoOpPropertyDetailsLogger,
    ): PropertyDetailsViewModel = DefaultPropertyDetailsViewModel(
        getPropertyDetailsUseCase = GetPropertyDetailsUseCase(
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
        ),
        watchlistRepository = watchlistRepository,
        logger = logger,
    )

    private fun property(
        id: String,
        areaId: String = "area-jvc",
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

    private class RecordingPropertyDetailsLogger : PropertyDetailsLogger {
        val events = mutableListOf<PropertyDetailsLogEvent>()

        override fun log(event: PropertyDetailsLogEvent) {
            events += event
        }
    }

    private class FakePropertyRepository(
        var propertiesResult: RepositoryResult<List<Property>>,
    ) : PropertyRepository {
        override fun getAllProperties(): RepositoryResult<List<Property>> = propertiesResult

        override fun getPropertyById(id: String): RepositoryResult<Property> {
            val properties = (propertiesResult as? RepositoryResult.Success)?.value.orEmpty()
            return properties.firstOrNull { it.id == id }?.let { RepositoryResult.Success(it) }
                ?: RepositoryResult.Failure(
                    if (propertiesResult is RepositoryResult.Failure) {
                        (propertiesResult as RepositoryResult.Failure).error
                    } else {
                        RepositoryError.NotFound
                    },
                )
        }

        override fun getPropertiesByAreaId(areaId: String): RepositoryResult<List<Property>> {
            val properties = (propertiesResult as? RepositoryResult.Success)?.value.orEmpty()
                .filter { it.areaId == areaId }
            return RepositoryResult.Success(properties)
        }
    }

    private class FakeWatchlistRepository(
        savedProperties: List<Property>,
    ) : WatchlistRepository {
        val savedPropertyIds = savedProperties.map { it.id }.toMutableSet()

        override fun getSavedProperties(): RepositoryResult<List<Property>> {
            return RepositoryResult.Success(savedPropertyIds.map { savedProperty(it) })
        }

        override fun getSavedAreas(): RepositoryResult<List<Area>> {
            return RepositoryResult.Success(emptyList())
        }

        override fun saveProperty(propertyId: String): RepositoryResult<Unit> {
            if (!savedPropertyIds.add(propertyId)) {
                return RepositoryResult.Failure(RepositoryError.AlreadyExists)
            }
            return RepositoryResult.Success(Unit)
        }

        override fun removeProperty(propertyId: String): RepositoryResult<Unit> {
            if (!savedPropertyIds.remove(propertyId)) {
                return RepositoryResult.Failure(RepositoryError.NotFound)
            }
            return RepositoryResult.Success(Unit)
        }

        override fun saveArea(areaId: String): RepositoryResult<Unit> = RepositoryResult.Success(Unit)

        override fun removeArea(areaId: String): RepositoryResult<Unit> = RepositoryResult.Success(Unit)

        private fun savedProperty(id: String): Property = Property(
            id = id,
            title = id.uppercase(),
            areaId = "area-jvc",
            price = Money(1_100_000.0),
            sizeSqft = 780.0,
            expectedAnnualRent = null,
            annualCosts = null,
            propertyType = PropertyType.Apartment,
            completionStatus = CompletionStatus.Ready,
        )
    }
}
