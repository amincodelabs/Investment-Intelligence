package amin.codelabs.realestateinvestmentintelligence.data.repository

import amin.codelabs.realestateinvestmentintelligence.data.mock.MockAreaDataSource
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MockAreaRepositoryTest {
    private val repository = MockAreaRepository()

    @Test
    fun `getAllAreas returns Dubai demo areas`() {
        val result = repository.getAllAreas().requireSuccess()

        assertEquals(6, result.size)
        assertTrue(result.any { it.name == "Dubai Marina" })
        assertTrue(result.any { it.name == "Jumeirah Village Circle" })
        assertTrue(result.any { it.name == "Downtown Dubai" })
        assertTrue(result.any { it.name == "Business Bay" })
        assertTrue(result.any { it.name == "Dubai Hills Estate" })
    }

    @Test
    fun `getAreaById returns area when id exists`() {
        val result = repository.getAreaById(MockAreaDataSource.DUBAI_MARINA_ID).requireSuccess()

        assertEquals("Dubai Marina", result.name)
    }

    @Test
    fun `getAreaById returns not found when id does not exist`() {
        val result = repository.getAreaById("missing-area").requireFailure()

        assertEquals(RepositoryError.NotFound, result)
    }

    private fun <T> RepositoryResult<T>.requireSuccess(): T =
        (this as RepositoryResult.Success).value

    private fun RepositoryResult<*>.requireFailure(): RepositoryError =
        (this as RepositoryResult.Failure).error
}
