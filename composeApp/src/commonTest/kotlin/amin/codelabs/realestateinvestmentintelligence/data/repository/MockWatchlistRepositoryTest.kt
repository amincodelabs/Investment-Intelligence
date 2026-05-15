package amin.codelabs.realestateinvestmentintelligence.data.repository

import amin.codelabs.realestateinvestmentintelligence.data.mock.MockAreaDataSource
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MockWatchlistRepositoryTest {
    private val repository = MockWatchlistRepository()

    @Test
    fun `getSavedProperties returns empty list initially`() {
        val result = repository.getSavedProperties().requireSuccess()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `saveProperty adds property to saved properties`() {
        val saveResult = repository.saveProperty(PROPERTY_ID).requireSuccess()
        val savedProperties = repository.getSavedProperties().requireSuccess()

        assertEquals(Unit, saveResult)
        assertEquals(listOf(PROPERTY_ID), savedProperties.map { it.id })
    }

    @Test
    fun `removeProperty removes saved property`() {
        repository.saveProperty(PROPERTY_ID)

        repository.removeProperty(PROPERTY_ID).requireSuccess()

        assertTrue(repository.getSavedProperties().requireSuccess().isEmpty())
    }

    @Test
    fun `saveProperty prevents duplicate property items`() {
        repository.saveProperty(PROPERTY_ID).requireSuccess()

        val result = repository.saveProperty(PROPERTY_ID).requireFailure()

        assertEquals(RepositoryError.AlreadyExists, result)
        assertEquals(1, repository.getSavedProperties().requireSuccess().size)
    }

    @Test
    fun `saveProperty returns not found for missing property`() {
        val result = repository.saveProperty("missing-property").requireFailure()

        assertEquals(RepositoryError.NotFound, result)
    }

    @Test
    fun `removeProperty returns not found when property is not saved`() {
        val result = repository.removeProperty(PROPERTY_ID).requireFailure()

        assertEquals(RepositoryError.NotFound, result)
    }

    @Test
    fun `getSavedAreas returns empty list initially`() {
        val result = repository.getSavedAreas().requireSuccess()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `saveArea adds area to saved areas`() {
        val saveResult = repository.saveArea(AREA_ID).requireSuccess()
        val savedAreas = repository.getSavedAreas().requireSuccess()

        assertEquals(Unit, saveResult)
        assertEquals(listOf(AREA_ID), savedAreas.map { it.id })
    }

    @Test
    fun `removeArea removes saved area`() {
        repository.saveArea(AREA_ID)

        repository.removeArea(AREA_ID).requireSuccess()

        assertTrue(repository.getSavedAreas().requireSuccess().isEmpty())
    }

    @Test
    fun `saveArea prevents duplicate area items`() {
        repository.saveArea(AREA_ID).requireSuccess()

        val result = repository.saveArea(AREA_ID).requireFailure()

        assertEquals(RepositoryError.AlreadyExists, result)
        assertEquals(1, repository.getSavedAreas().requireSuccess().size)
    }

    @Test
    fun `saveArea returns not found for missing area`() {
        val result = repository.saveArea("missing-area").requireFailure()

        assertEquals(RepositoryError.NotFound, result)
    }

    @Test
    fun `removeArea returns not found when area is not saved`() {
        val result = repository.removeArea(AREA_ID).requireFailure()

        assertEquals(RepositoryError.NotFound, result)
    }

    private fun <T> RepositoryResult<T>.requireSuccess(): T =
        (this as RepositoryResult.Success).value

    private fun RepositoryResult<*>.requireFailure(): RepositoryError =
        (this as RepositoryResult.Failure).error

    private companion object {
        const val PROPERTY_ID = "property-jvc-studio"
        const val AREA_ID = MockAreaDataSource.JVC_ID
    }
}
