package amin.codelabs.realestateinvestmentintelligence.calculator.presentation

import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentMetrics
import amin.codelabs.realestateinvestmentintelligence.domain.model.Money
import amin.codelabs.realestateinvestmentintelligence.domain.service.InvestmentCalculationError
import amin.codelabs.realestateinvestmentintelligence.domain.service.InvestmentCalculationInput
import amin.codelabs.realestateinvestmentintelligence.domain.service.InvestmentCalculationResult
import amin.codelabs.realestateinvestmentintelligence.domain.service.InvestmentCalculator
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface InvestmentCalculatorViewModel {
    val state: InvestmentCalculatorUiState
    val effect: InvestmentCalculatorUiEffect?

    fun onEvent(event: InvestmentCalculatorUiEvent)
    fun consumeEffect()
}

class DefaultInvestmentCalculatorViewModel(
    private val logger: InvestmentCalculatorLogger = NoOpInvestmentCalculatorLogger,
) : InvestmentCalculatorViewModel {
    override var state by mutableStateOf(InvestmentCalculatorUiState())
        private set

    override var effect by mutableStateOf<InvestmentCalculatorUiEffect?>(null)
        private set

    override fun onEvent(event: InvestmentCalculatorUiEvent) {
        when (event) {
            is InvestmentCalculatorUiEvent.PropertyPriceChanged -> updateForm { copy(propertyPrice = event.value) }
            is InvestmentCalculatorUiEvent.AnnualRentChanged -> updateForm { copy(annualRent = event.value) }
            is InvestmentCalculatorUiEvent.ServiceChargesChanged -> updateForm { copy(serviceCharges = event.value) }
            is InvestmentCalculatorUiEvent.OtherCostsChanged -> updateForm { copy(otherCosts = event.value) }
            is InvestmentCalculatorUiEvent.SizeSqftChanged -> updateForm { copy(sizeSqft = event.value) }
            is InvestmentCalculatorUiEvent.OccupancyRateChanged -> updateForm { copy(occupancyRate = event.value) }
            is InvestmentCalculatorUiEvent.AppreciationRateChanged -> updateForm { copy(appreciationRate = event.value) }
            InvestmentCalculatorUiEvent.CalculateClicked -> calculate()
            InvestmentCalculatorUiEvent.ResetClicked -> reset()
        }
    }

    override fun consumeEffect() {
        effect = null
    }

    private fun updateForm(update: InvestmentCalculatorForm.() -> InvestmentCalculatorForm) {
        val content = state
        state = content.copy(
            form = content.form.update(),
            validationErrors = InvestmentCalculatorValidationErrors(),
            result = null,
            isError = false,
            errorMessage = null,
        )
    }

    private fun calculate() {
        logger.log(InvestmentCalculatorLogEvent.CalculationStarted)
        state = state.copy(isLoading = true, isError = false, errorMessage = null)

        val calculationInput = state.form.toCalculationInput()
        when (val result = InvestmentCalculator.calculate(calculationInput.input)) {
            is InvestmentCalculationResult.Complete -> {
                if (calculationInput.annualCostsFieldErrors.hasErrors()) {
                    logger.log(InvestmentCalculatorLogEvent.ValidationFailed)
                    state = state.copy(
                        isLoading = false,
                        validationErrors = calculationInput.annualCostsFieldErrors,
                        result = null,
                        isError = true,
                        errorMessage = "Review the highlighted inputs and calculate again.",
                    )
                    return
                }
                logger.log(InvestmentCalculatorLogEvent.CalculationSucceeded)
                state = state.copy(
                    isLoading = false,
                    validationErrors = InvestmentCalculatorValidationErrors(),
                    result = result.metrics.toUi(currency = Money.DEFAULT_CURRENCY),
                )
                effect = InvestmentCalculatorUiEffect.CalculationCompleted
            }

            is InvestmentCalculationResult.Incomplete -> {
                logger.log(InvestmentCalculatorLogEvent.ValidationFailed)
                state = state.copy(
                    isLoading = false,
                    validationErrors = result.errors.toValidationErrors(calculationInput),
                    result = null,
                    isError = true,
                    errorMessage = "Review the highlighted inputs and calculate again.",
                )
            }
        }
    }

    private fun reset() {
        logger.log(InvestmentCalculatorLogEvent.Reset)
        state = InvestmentCalculatorUiState()
        effect = null
    }
}

@Immutable
data class InvestmentCalculatorUiState(
    val form: InvestmentCalculatorForm = InvestmentCalculatorForm(),
    val validationErrors: InvestmentCalculatorValidationErrors = InvestmentCalculatorValidationErrors(),
    val result: InvestmentCalculatorResultUi? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String? = null,
)

@Immutable
data class InvestmentCalculatorForm(
    val propertyPrice: String = "",
    val annualRent: String = "",
    val serviceCharges: String = "",
    val otherCosts: String = "",
    val sizeSqft: String = "",
    val occupancyRate: String = "100",
    val appreciationRate: String = "",
)

@Immutable
data class InvestmentCalculatorValidationErrors(
    val propertyPrice: String? = null,
    val annualRent: String? = null,
    val serviceCharges: String? = null,
    val otherCosts: String? = null,
    val sizeSqft: String? = null,
    val occupancyRate: String? = null,
    val appreciationRate: String? = null,
) {
    fun hasErrors(): Boolean = listOf(
        propertyPrice,
        annualRent,
        serviceCharges,
        otherCosts,
        sizeSqft,
        occupancyRate,
        appreciationRate,
    ).any { it != null }
}

@Immutable
data class InvestmentCalculatorResultUi(
    val currency: String,
    val grossRentalYieldPercentage: Double,
    val netRentalYieldPercentage: Double,
    val monthlyRentAmount: Double,
    val monthlyCostsAmount: Double,
    val monthlyCashFlowAmount: Double,
    val annualCashFlowAmount: Double,
    val pricePerSqftAmount: Double,
    val simpleRoiPercentage: Double?,
)

sealed interface InvestmentCalculatorUiEvent {
    data class PropertyPriceChanged(val value: String) : InvestmentCalculatorUiEvent
    data class AnnualRentChanged(val value: String) : InvestmentCalculatorUiEvent
    data class ServiceChargesChanged(val value: String) : InvestmentCalculatorUiEvent
    data class OtherCostsChanged(val value: String) : InvestmentCalculatorUiEvent
    data class SizeSqftChanged(val value: String) : InvestmentCalculatorUiEvent
    data class OccupancyRateChanged(val value: String) : InvestmentCalculatorUiEvent
    data class AppreciationRateChanged(val value: String) : InvestmentCalculatorUiEvent
    data object CalculateClicked : InvestmentCalculatorUiEvent
    data object ResetClicked : InvestmentCalculatorUiEvent
}

sealed interface InvestmentCalculatorUiEffect {
    data object CalculationCompleted : InvestmentCalculatorUiEffect
}

interface InvestmentCalculatorLogger {
    fun log(event: InvestmentCalculatorLogEvent)
}

enum class InvestmentCalculatorLogEvent {
    CalculationStarted,
    CalculationSucceeded,
    ValidationFailed,
    Reset,
}

object NoOpInvestmentCalculatorLogger : InvestmentCalculatorLogger {
    override fun log(event: InvestmentCalculatorLogEvent) = Unit
}

private data class ParsedCalculationInput(
    val input: InvestmentCalculationInput,
    val annualCostsFieldErrors: InvestmentCalculatorValidationErrors = InvestmentCalculatorValidationErrors(),
)

private fun InvestmentCalculatorForm.toCalculationInput(): ParsedCalculationInput {
    val serviceChargesAmount = serviceCharges.toOptionalDouble()
    val otherCostsAmount = otherCosts.toOptionalDouble()
    val annualCostsAmount = (serviceChargesAmount ?: 0.0) + (otherCostsAmount ?: 0.0)
    return ParsedCalculationInput(
        input = InvestmentCalculationInput(
            propertyPrice = propertyPrice.toMoneyOrNull(),
            annualRent = annualRent.toMoneyOrNull(),
            annualCosts = Money(annualCostsAmount),
            sizeSqft = sizeSqft.toDoubleOrNullFromInput(),
            occupancyRatePercentage = occupancyRate.toOptionalDouble(),
            appreciationRatePercentage = appreciationRate.toOptionalDouble(),
        ),
        annualCostsFieldErrors = InvestmentCalculatorValidationErrors(
            serviceCharges = serviceCharges.optionalZeroOrPositiveError(),
            otherCosts = otherCosts.optionalZeroOrPositiveError(),
            appreciationRate = appreciationRate.invalidNumberMessage(),
        ),
    )
}

private fun List<InvestmentCalculationError>.toValidationErrors(
    parsedInput: ParsedCalculationInput,
): InvestmentCalculatorValidationErrors {
    var errors = parsedInput.annualCostsFieldErrors
    forEach { error ->
        errors = when (error) {
            InvestmentCalculationError.MissingPropertyPrice -> errors.copy(propertyPrice = "Enter property price.")
            InvestmentCalculationError.InvalidPropertyPrice -> errors.copy(propertyPrice = "Price must be greater than zero.")
            InvestmentCalculationError.MissingAnnualRent -> errors.copy(annualRent = "Enter expected annual rent.")
            InvestmentCalculationError.InvalidAnnualRent -> errors.copy(annualRent = "Annual rent cannot be negative.")
            InvestmentCalculationError.InvalidAnnualCosts -> errors.copy(
                serviceCharges = errors.serviceCharges ?: "Costs cannot be negative.",
            )
            InvestmentCalculationError.MissingSizeSqft -> errors.copy(sizeSqft = "Enter property size.")
            InvestmentCalculationError.InvalidSizeSqft -> errors.copy(sizeSqft = "Size must be greater than zero.")
            InvestmentCalculationError.InvalidOccupancyRate -> errors.copy(occupancyRate = "Occupancy must be between 0 and 100.")
            InvestmentCalculationError.InvalidAppreciationRate -> errors.copy(appreciationRate = "Enter a valid appreciation rate.")
            InvestmentCalculationError.InvalidMonthlyMortgagePayment,
            InvestmentCalculationError.InvalidInitialInvestment,
            InvestmentCalculationError.CurrencyMismatch,
            -> errors
        }
    }
    return errors
}

private fun InvestmentMetrics.toUi(currency: String): InvestmentCalculatorResultUi {
    return InvestmentCalculatorResultUi(
        currency = currency,
        grossRentalYieldPercentage = grossRentalYield.percentage,
        netRentalYieldPercentage = netRentalYield.percentage,
        monthlyRentAmount = monthlyRent.amount,
        monthlyCostsAmount = monthlyCosts.amount,
        monthlyCashFlowAmount = monthlyCashFlow.amount,
        annualCashFlowAmount = annualCashFlow.amount,
        pricePerSqftAmount = pricePerSqft.amount,
        simpleRoiPercentage = simpleRoiPercentage,
    )
}

private fun String.toMoneyOrNull(): Money? {
    val value = toDoubleOrNullFromInput() ?: return null
    return Money(value)
}

private fun String.toOptionalDouble(): Double? {
    if (isBlank()) return null
    return toDoubleOrNullFromInput()
}

private fun String.toDoubleOrNullFromInput(): Double? {
    return trim().replace(",", "").toDoubleOrNull()
}

private fun String.invalidNumberMessage(): String? {
    if (isBlank()) return null
    return if (toDoubleOrNullFromInput() == null) "Enter a valid number." else null
}

private fun String.optionalZeroOrPositiveError(): String? {
    if (isBlank()) return null
    val value = toDoubleOrNullFromInput() ?: return "Enter a valid number."
    return if (value < 0.0) "Costs cannot be negative." else null
}
