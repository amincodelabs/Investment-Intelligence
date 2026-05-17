package amin.codelabs.realestateinvestmentintelligence.calculator.presentation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InvestmentCalculatorViewModelTest {
    @Test
    fun `calculator starts with empty content state`() {
        val viewModel = createViewModel()

        assertEquals(InvestmentCalculatorUiState(), viewModel.state)
        assertNull(viewModel.effect)
    }

    @Test
    fun `valid calculation returns result metrics`() {
        val viewModel = createViewModel()

        enterValidInputs(viewModel)
        viewModel.onEvent(InvestmentCalculatorUiEvent.CalculateClicked)

        val result = assertNotNull(viewModel.state.result)
        assertDoubleEquals(8.0, result.grossRentalYieldPercentage)
        assertDoubleEquals(6.8, result.netRentalYieldPercentage)
        assertDoubleEquals(5_666.6666, result.monthlyCashFlowAmount)
        assertEquals(InvestmentCalculatorUiEffect.CalculationCompleted, viewModel.effect)
    }

    @Test
    fun `invalid inputs return visible validation errors`() {
        val viewModel = createViewModel()

        viewModel.onEvent(InvestmentCalculatorUiEvent.PropertyPriceChanged("abc"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.AnnualRentChanged("-1"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.ServiceChargesChanged("cost"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.SizeSqftChanged("0"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.OccupancyRateChanged("120"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.CalculateClicked)

        assertTrue(viewModel.state.isError)
        assertNull(viewModel.state.result)
        assertEquals("Enter property price.", viewModel.state.validationErrors.propertyPrice)
        assertEquals("Annual rent cannot be negative.", viewModel.state.validationErrors.annualRent)
        assertEquals("Enter a valid number.", viewModel.state.validationErrors.serviceCharges)
        assertEquals("Size must be greater than zero.", viewModel.state.validationErrors.sizeSqft)
        assertEquals("Occupancy must be between 0 and 100.", viewModel.state.validationErrors.occupancyRate)
    }

    @Test
    fun `zero and negative price return price validation error`() {
        val viewModel = createViewModel()

        enterValidInputs(viewModel)
        viewModel.onEvent(InvestmentCalculatorUiEvent.PropertyPriceChanged("0"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.CalculateClicked)

        assertEquals("Price must be greater than zero.", viewModel.state.validationErrors.propertyPrice)

        viewModel.onEvent(InvestmentCalculatorUiEvent.PropertyPriceChanged("-1"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.CalculateClicked)

        assertEquals("Price must be greater than zero.", viewModel.state.validationErrors.propertyPrice)
    }

    @Test
    fun `negative cash flow is preserved in result`() {
        val viewModel = createViewModel()

        viewModel.onEvent(InvestmentCalculatorUiEvent.PropertyPriceChanged("1000000"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.AnnualRentChanged("60000"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.ServiceChargesChanged("72000"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.SizeSqftChanged("1000"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.CalculateClicked)

        val result = assertNotNull(viewModel.state.result)
        assertDoubleEquals(-1_000.0, result.monthlyCashFlowAmount)
        assertDoubleEquals(-12_000.0, result.annualCashFlowAmount)
    }

    @Test
    fun `reset clears inputs result errors and effect`() {
        val viewModel = createViewModel()

        enterValidInputs(viewModel)
        viewModel.onEvent(InvestmentCalculatorUiEvent.CalculateClicked)
        assertNotNull(viewModel.state.result)

        viewModel.onEvent(InvestmentCalculatorUiEvent.ResetClicked)

        assertEquals(InvestmentCalculatorUiState(), viewModel.state)
        assertNull(viewModel.effect)
    }

    @Test
    fun `input changes clear previous result and error state`() {
        val viewModel = createViewModel()

        viewModel.onEvent(InvestmentCalculatorUiEvent.CalculateClicked)
        assertTrue(viewModel.state.isError)

        viewModel.onEvent(InvestmentCalculatorUiEvent.PropertyPriceChanged("1000000"))

        assertFalse(viewModel.state.isError)
        assertFalse(viewModel.state.validationErrors.hasErrors())
        assertNull(viewModel.state.result)
    }

    @Test
    fun `logger receives calculation transitions`() {
        val logger = RecordingInvestmentCalculatorLogger()
        val viewModel = createViewModel(logger)

        enterValidInputs(viewModel)
        viewModel.onEvent(InvestmentCalculatorUiEvent.CalculateClicked)
        viewModel.onEvent(InvestmentCalculatorUiEvent.ResetClicked)

        assertEquals(
            listOf(
                InvestmentCalculatorLogEvent.CalculationStarted,
                InvestmentCalculatorLogEvent.CalculationSucceeded,
                InvestmentCalculatorLogEvent.Reset,
            ),
            logger.events,
        )
    }

    private fun createViewModel(
        logger: InvestmentCalculatorLogger = NoOpInvestmentCalculatorLogger,
    ): InvestmentCalculatorViewModel = DefaultInvestmentCalculatorViewModel(logger)

    private fun enterValidInputs(viewModel: InvestmentCalculatorViewModel) {
        viewModel.onEvent(InvestmentCalculatorUiEvent.PropertyPriceChanged("1000000"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.AnnualRentChanged("80000"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.ServiceChargesChanged("12000"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.SizeSqftChanged("1000"))
        viewModel.onEvent(InvestmentCalculatorUiEvent.OccupancyRateChanged("100"))
    }

    private class RecordingInvestmentCalculatorLogger : InvestmentCalculatorLogger {
        val events = mutableListOf<InvestmentCalculatorLogEvent>()

        override fun log(event: InvestmentCalculatorLogEvent) {
            events += event
        }
    }

    private fun assertDoubleEquals(expected: Double, actual: Double) {
        assertEquals(expected, actual, absoluteTolerance = 0.0001)
    }
}
