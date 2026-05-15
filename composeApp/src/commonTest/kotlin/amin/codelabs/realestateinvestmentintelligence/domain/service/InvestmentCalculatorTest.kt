package amin.codelabs.realestateinvestmentintelligence.domain.service

import amin.codelabs.realestateinvestmentintelligence.domain.model.Money
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InvestmentCalculatorTest {
    @Test
    fun `calculate returns gross rental yield when price and annual rent are valid`() {
        val result = InvestmentCalculator.calculate(validInput())

        val metrics = result.requireMetrics()
        assertDoubleEquals(8.0, metrics.grossRentalYield.percentage)
    }

    @Test
    fun `calculate returns net rental yield after annual costs`() {
        val result = InvestmentCalculator.calculate(
            validInput(
                propertyPrice = Money(1_000_000.0),
                annualRent = Money(80_000.0),
                annualCosts = Money(15_000.0),
            ),
        )

        val metrics = result.requireMetrics()
        assertDoubleEquals(6.5, metrics.netRentalYield.percentage)
    }

    @Test
    fun `calculate returns monthly rent from annual rent`() {
        val result = InvestmentCalculator.calculate(
            validInput(annualRent = Money(120_000.0)),
        )

        val metrics = result.requireMetrics()
        assertDoubleEquals(10_000.0, metrics.monthlyRent.amount)
    }

    @Test
    fun `calculate returns monthly costs from annual costs`() {
        val result = InvestmentCalculator.calculate(
            validInput(annualCosts = Money(24_000.0)),
        )

        val metrics = result.requireMetrics()
        assertDoubleEquals(2_000.0, metrics.monthlyCosts.amount)
    }

    @Test
    fun `calculate returns monthly cash flow after monthly costs and mortgage payment`() {
        val result = InvestmentCalculator.calculate(
            validInput(
                annualRent = Money(120_000.0),
                annualCosts = Money(24_000.0),
                monthlyMortgagePayment = Money(3_000.0),
            ),
        )

        val metrics = result.requireMetrics()
        assertDoubleEquals(5_000.0, metrics.monthlyCashFlow.amount)
    }

    @Test
    fun `calculate returns annual cash flow after costs and annual mortgage payments`() {
        val result = InvestmentCalculator.calculate(
            validInput(
                annualRent = Money(120_000.0),
                annualCosts = Money(24_000.0),
                monthlyMortgagePayment = Money(3_000.0),
            ),
        )

        val metrics = result.requireMetrics()
        assertDoubleEquals(60_000.0, metrics.annualCashFlow.amount)
    }

    @Test
    fun `calculate returns price per sqft when size is valid`() {
        val result = InvestmentCalculator.calculate(
            validInput(
                propertyPrice = Money(1_200_000.0),
                sizeSqft = 800.0,
            ),
        )

        val metrics = result.requireMetrics()
        assertDoubleEquals(1_500.0, metrics.pricePerSqft.amount)
    }

    @Test
    fun `calculate returns simple roi when appreciation rate is provided`() {
        val result = InvestmentCalculator.calculate(
            validInput(
                propertyPrice = Money(1_000_000.0),
                annualRent = Money(80_000.0),
                annualCosts = Money(20_000.0),
                appreciationRatePercentage = 5.0,
            ),
        )

        val metrics = result.requireMetrics()
        assertDoubleEquals(11.0, metrics.simpleRoiPercentage)
    }

    @Test
    fun `calculate leaves simple roi empty when appreciation rate is missing`() {
        val result = InvestmentCalculator.calculate(
            validInput(appreciationRatePercentage = null),
        )

        val metrics = result.requireMetrics()
        assertNull(metrics.simpleRoiPercentage)
    }

    @Test
    fun `calculate returns invalid property price for zero price`() {
        val result = InvestmentCalculator.calculate(
            validInput(propertyPrice = Money(0.0)),
        )

        val incomplete = result.requireIncomplete()
        assertTrue(InvestmentCalculationError.InvalidPropertyPrice in incomplete.errors)
    }

    @Test
    fun `calculate returns invalid property price for negative price`() {
        val result = InvestmentCalculator.calculate(
            validInput(propertyPrice = Money(-1.0)),
        )

        val incomplete = result.requireIncomplete()
        assertTrue(InvestmentCalculationError.InvalidPropertyPrice in incomplete.errors)
    }

    @Test
    fun `calculate returns missing required input errors`() {
        val result = InvestmentCalculator.calculate(
            InvestmentCalculationInput(
                propertyPrice = null,
                annualRent = null,
                sizeSqft = null,
            ),
        )

        val incomplete = result.requireIncomplete()
        assertEquals(
            listOf(
                InvestmentCalculationError.MissingPropertyPrice,
                InvestmentCalculationError.MissingAnnualRent,
                InvestmentCalculationError.MissingSizeSqft,
            ),
            incomplete.errors,
        )
    }

    @Test
    fun `calculate returns invalid input errors for annual rent costs and size`() {
        val result = InvestmentCalculator.calculate(
            validInput(
                annualRent = Money(-1.0),
                annualCosts = Money(-1.0),
                sizeSqft = 0.0,
            ),
        )

        val incomplete = result.requireIncomplete()
        assertTrue(InvestmentCalculationError.InvalidAnnualRent in incomplete.errors)
        assertTrue(InvestmentCalculationError.InvalidAnnualCosts in incomplete.errors)
        assertTrue(InvestmentCalculationError.InvalidSizeSqft in incomplete.errors)
    }

    @Test
    fun `calculate preserves negative monthly cash flow`() {
        val result = InvestmentCalculator.calculate(
            validInput(
                annualRent = Money(60_000.0),
                annualCosts = Money(24_000.0),
                monthlyMortgagePayment = Money(4_000.0),
            ),
        )

        val metrics = result.requireMetrics()
        assertDoubleEquals(-1_000.0, metrics.monthlyCashFlow.amount)
        assertDoubleEquals(-12_000.0, metrics.annualCashFlow.amount)
    }

    @Test
    fun `calculate returns currency mismatch for mixed money currencies`() {
        val result = InvestmentCalculator.calculate(
            validInput(
                propertyPrice = Money(1_000_000.0, currency = "AED"),
                annualRent = Money(80_000.0, currency = "USD"),
            ),
        )

        val incomplete = result.requireIncomplete()
        assertTrue(InvestmentCalculationError.CurrencyMismatch in incomplete.errors)
    }

    private fun validInput(
        propertyPrice: Money = Money(1_000_000.0),
        annualRent: Money = Money(80_000.0),
        annualCosts: Money = Money(12_000.0),
        sizeSqft: Double = 1_000.0,
        monthlyMortgagePayment: Money? = null,
        appreciationRatePercentage: Double? = null,
        initialInvestment: Money? = null,
    ) = InvestmentCalculationInput(
        propertyPrice = propertyPrice,
        annualRent = annualRent,
        annualCosts = annualCosts,
        sizeSqft = sizeSqft,
        monthlyMortgagePayment = monthlyMortgagePayment,
        appreciationRatePercentage = appreciationRatePercentage,
        initialInvestment = initialInvestment,
    )

    private fun InvestmentCalculationResult.requireMetrics() =
        (this as InvestmentCalculationResult.Complete).metrics

    private fun InvestmentCalculationResult.requireIncomplete() =
        this as InvestmentCalculationResult.Incomplete

    private fun assertDoubleEquals(expected: Double, actual: Double?) {
        val actualValue = requireNotNull(actual)
        assertEquals(expected, actualValue, absoluteTolerance = 0.0001)
    }
}
