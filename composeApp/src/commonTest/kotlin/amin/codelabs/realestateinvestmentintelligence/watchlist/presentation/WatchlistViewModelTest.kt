package amin.codelabs.realestateinvestmentintelligence.watchlist.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.CompletionStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.Money
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.model.PropertyType
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WatchlistViewModelTest {
    @Test
    fun `watchlist view model starts loading`() {
        val viewModel = createViewModel()

        assertEquals(WatchlistUiState.Loading, viewModel.state)
        assertNull(viewModel.effect)
    }

    @Test
    fun `load saved items returns content`() {
        val viewModel = createViewModel(
            repository = FakeWatchlistRepository(
                savedPropertiesList = listOf(property("property-1")),
                savedAreasList = listOf(area("area-1")),
            ),
        )

        viewModel.onEvent(WatchlistUiEvent.LoadWatchlist)

        val state = assertIs<WatchlistUiState.Content>(viewModel.state)
        assertEquals("property-1", state.content.savedProperties.single().id)
        assertEquals("area-1", state.content.savedAreas.single().id)
    }

    @Test
    fun `load saved items returns empty state when nothing is saved`() {
        val viewModel = createViewModel()

        viewModel.onEvent(WatchlistUiEvent.LoadWatchlist)

        assertEquals(WatchlistUiState.Empty, viewModel.state)
    }

    @Test
    fun `remove property updates content and empties state when last item is removed`() {
        val repository = FakeWatchlistRepository(
            savedPropertiesList = listOf(property("property-1")),
            savedAreasList = listOf(area("area-1")),
        )
        val viewModel = createViewModel(repository = repository)

        viewModel.onEvent(WatchlistUiEvent.LoadWatchlist)
        viewModel.onEvent(WatchlistUiEvent.RemovePropertyClicked("property-1"))

        val state = assertIs<WatchlistUiState.Content>(viewModel.state)
        assertTrue(state.content.savedProperties.isEmpty())
        assertFalse(state.content.savedAreas.isEmpty())

        viewModel.onEvent(WatchlistUiEvent.RemoveAreaClicked("area-1"))
        assertEquals(WatchlistUiState.Empty, viewModel.state)
    }

    @Test
    fun `remove area updates content and empties state when last item is removed`() {
        val repository = FakeWatchlistRepository(
            savedPropertiesList = listOf(property("property-1")),
            savedAreasList = listOf(area("area-1")),
        )
        val viewModel = createViewModel(repository = repository)

        viewModel.onEvent(WatchlistUiEvent.LoadWatchlist)
        viewModel.onEvent(WatchlistUiEvent.RemoveAreaClicked("area-1"))

        val state = assertIs<WatchlistUiState.Content>(viewModel.state)
        assertTrue(state.content.savedAreas.isEmpty())
        assertFalse(state.content.savedProperties.isEmpty())

        viewModel.onEvent(WatchlistUiEvent.RemovePropertyClicked("property-1"))
        assertEquals(WatchlistUiState.Empty, viewModel.state)
    }

    @Test
    fun `error state is shown when loading fails and retry recovers`() {
        val repository = FakeWatchlistRepository(
            savedPropertiesResult = RepositoryResult.Failure(RepositoryError.Unknown),
        )
        val viewModel = createViewModel(repository = repository)

        viewModel.onEvent(WatchlistUiEvent.LoadWatchlist)

        val errorState = assertIs<WatchlistUiState.Error>(viewModel.state)
        assertEquals("Unable to load watchlist. Please try again.", errorState.message)

        repository.savedPropertiesResult = RepositoryResult.Success(listOf(property("property-1")))
        repository.savedAreasResult = RepositoryResult.Success(listOf(area("area-1")))
        viewModel.onEvent(WatchlistUiEvent.RetryClicked)

        val contentState = assertIs<WatchlistUiState.Content>(viewModel.state)
        assertEquals(1, contentState.content.savedProperties.size)
        assertEquals(1, contentState.content.savedAreas.size)
    }

    @Test
    fun `property and area clicks emit navigation effects`() {
        val viewModel = createViewModel()

        viewModel.onEvent(WatchlistUiEvent.PropertyClicked("property-1"))
        assertEquals(
            WatchlistUiEffect.NavigateToPropertyDetails("property-1"),
            viewModel.effect,
        )
        viewModel.consumeEffect()

        viewModel.onEvent(WatchlistUiEvent.AreaClicked("area-1"))
        assertEquals(WatchlistUiEffect.NavigateToAreaDetails("area-1"), viewModel.effect)
    }

    private fun createViewModel(
        repository: FakeWatchlistRepository = FakeWatchlistRepository(),
    ): WatchlistViewModel = DefaultWatchlistViewModel(repository)

    private fun property(id: String): Property = Property(
        id = id,
        title = id.uppercase(),
        areaId = "area-1",
        price = Money(1_000_000.0),
        sizeSqft = 900.0,
        expectedAnnualRent = Money(80_000.0),
        annualCosts = Money(12_000.0),
        propertyType = PropertyType.Apartment,
        completionStatus = CompletionStatus.Ready,
        riskLevel = RiskLevel.Medium,
        demandLevel = DemandLevel.High,
    )

    private fun area(id: String): Area = Area(
        id = id,
        name = id.uppercase(),
        averagePropertyPrice = Money(1_200_000.0),
        averageRentalYield = amin.codelabs.realestateinvestmentintelligence.domain.model.RentalYield(7.0),
        demandLevel = DemandLevel.High,
        riskLevel = RiskLevel.Medium,
        investmentScore = amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentScore(82),
    )

    private class FakeWatchlistRepository(
        savedPropertiesList: List<Property> = emptyList(),
        savedAreasList: List<Area> = emptyList(),
        var savedPropertiesResult: RepositoryResult<List<Property>> = RepositoryResult.Success(savedPropertiesList),
        var savedAreasResult: RepositoryResult<List<Area>> = RepositoryResult.Success(savedAreasList),
    ) : WatchlistRepository {
        private val savedPropertyIds = mutableSetOf<String>()
        private val savedAreaIds = mutableSetOf<String>()

        override fun getSavedProperties(): RepositoryResult<List<Property>> {
            val result = savedPropertiesResult
            return when (result) {
                is RepositoryResult.Success -> {
                    val ids = result.value.map { it.id }.toMutableSet()
                    savedPropertyIds.clear()
                    savedPropertyIds.addAll(ids)
                    RepositoryResult.Success(result.value)
                }
                is RepositoryResult.Failure -> result
            }
        }

        override fun getSavedAreas(): RepositoryResult<List<Area>> {
            val result = savedAreasResult
            return when (result) {
                is RepositoryResult.Success -> {
                    val ids = result.value.map { it.id }.toMutableSet()
                    savedAreaIds.clear()
                    savedAreaIds.addAll(ids)
                    RepositoryResult.Success(result.value)
                }
                is RepositoryResult.Failure -> result
            }
        }

        override fun saveProperty(propertyId: String): RepositoryResult<Unit> {
            savedPropertyIds += propertyId
            return RepositoryResult.Success(Unit)
        }

        override fun removeProperty(propertyId: String): RepositoryResult<Unit> {
            if (!savedPropertyIds.remove(propertyId)) {
                return RepositoryResult.Failure(RepositoryError.NotFound)
            }
            savedPropertiesResult = when (val result = savedPropertiesResult) {
                is RepositoryResult.Success -> RepositoryResult.Success(
                    result.value.filterNot { it.id == propertyId },
                )
                is RepositoryResult.Failure -> result
            }
            return RepositoryResult.Success(Unit)
        }

        override fun saveArea(areaId: String): RepositoryResult<Unit> {
            savedAreaIds += areaId
            return RepositoryResult.Success(Unit)
        }

        override fun removeArea(areaId: String): RepositoryResult<Unit> {
            if (!savedAreaIds.remove(areaId)) {
                return RepositoryResult.Failure(RepositoryError.NotFound)
            }
            savedAreasResult = when (val result = savedAreasResult) {
                is RepositoryResult.Success -> RepositoryResult.Success(
                    result.value.filterNot { it.id == areaId },
                )
                is RepositoryResult.Failure -> result
            }
            return RepositoryResult.Success(Unit)
        }
    }
}
