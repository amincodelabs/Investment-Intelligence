package amin.codelabs.realestateinvestmentintelligence.data.repository

import amin.codelabs.realestateinvestmentintelligence.data.mock.MockAreaDataSource
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryError
import amin.codelabs.realestateinvestmentintelligence.domain.repository.RepositoryResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MockPropertyRepositoryTest {
    private val repository = MockPropertyRepository()

    @Test
    fun `getAllProperties returns realistic Dubai demo properties`() {
        val result = repository.getAllProperties().requireSuccess()

        assertEquals(6, result.size)
        assertTrue(result.all { it.price.amount > 0.0 })
        assertTrue(result.all { it.sizeSqft > 0.0 })
        assertTrue(result.all { it.expectedAnnualRent != null })
    }

    @Test
    fun `getPropertyById returns property when id exists`() {
        val result = repository.getPropertyById("property-jvc-studio").requireSuccess()

        assertEquals("High-yield studio in JVC", result.title)
        assertEquals(MockAreaDataSource.JVC_ID, result.areaId)
    }

    @Test
    fun `getPropertyById returns not found when id does not exist`() {
        val result = repository.getPropertyById("missing-property").requireFailure()

        assertEquals(RepositoryError.NotFound, result)
    }

    @Test
    fun `getPropertiesByAreaId returns properties for area`() {
        val result = repository.getPropertiesByAreaId(MockAreaDataSource.BUSINESS_BAY_ID).requireSuccess()

        assertEquals(1, result.size)
        assertEquals("property-business-bay-one-bedroom", result.first().id)
    }

    @Test
    fun `getPropertiesByAreaId returns not found when area has no properties`() {
        val result = repository.getPropertiesByAreaId("missing-area").requireFailure()

        assertEquals(RepositoryError.NotFound, result)
    }

    @Test
    fun `property mock metrics are internally consistent with formulas`() {
        val result = repository.getPropertyById("property-marina-one-bedroom").requireSuccess()
        val metrics = requireNotNull(result.investmentMetrics)

        assertDoubleEquals(7.8378, metrics.grossRentalYield.percentage)
        assertDoubleEquals(6.3243, metrics.netRentalYield.percentage)
        assertDoubleEquals(2_176.4705, metrics.pricePerSqft.amount)
    }

    private fun <T> RepositoryResult<T>.requireSuccess(): T =
        (this as RepositoryResult.Success).value

    private fun RepositoryResult<*>.requireFailure(): RepositoryError =
        (this as RepositoryResult.Failure).error

    private fun assertDoubleEquals(expected: Double, actual: Double) {
        assertEquals(expected, actual, absoluteTolerance = 0.0001)
    }
}
