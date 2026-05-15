package amin.codelabs.realestateinvestmentintelligence.domain.service

import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentMetrics
import amin.codelabs.realestateinvestmentintelligence.domain.model.Money
import amin.codelabs.realestateinvestmentintelligence.domain.model.RentalYield

object InvestmentCalculator {
    fun calculate(input: InvestmentCalculationInput): InvestmentCalculationResult {
        val errors = input.validate()
        if (errors.isNotEmpty()) {
            return InvestmentCalculationResult.Incomplete(errors)
        }

        val propertyPrice = input.propertyPrice ?: return InvestmentCalculationResult.Incomplete(
            listOf(InvestmentCalculationError.MissingPropertyPrice),
        )
        val annualRent = input.annualRent ?: return InvestmentCalculationResult.Incomplete(
            listOf(InvestmentCalculationError.MissingAnnualRent),
        )
        val annualCosts = input.annualCosts ?: Money.zero(propertyPrice.currency)
        val sizeSqft = input.sizeSqft ?: return InvestmentCalculationResult.Incomplete(
            listOf(InvestmentCalculationError.MissingSizeSqft),
        )
        val monthlyMortgagePayment = input.monthlyMortgagePayment ?: Money.zero(propertyPrice.currency)

        val monthlyRent = monthlyRent(annualRent)
        val monthlyCosts = monthlyCosts(annualCosts)
        val monthlyCashFlow = monthlyRent - monthlyCosts - monthlyMortgagePayment
        val annualCashFlow = annualCashFlow(
            annualRent = annualRent,
            annualCosts = annualCosts,
            annualMortgagePayments = monthlyMortgagePayment.amount * MONTHS_IN_YEAR,
        )

        return InvestmentCalculationResult.Complete(
            InvestmentMetrics(
                grossRentalYield = grossRentalYield(annualRent, propertyPrice),
                netRentalYield = netRentalYield(annualRent, annualCosts, propertyPrice),
                monthlyRent = monthlyRent,
                monthlyCosts = monthlyCosts,
                monthlyCashFlow = monthlyCashFlow,
                annualCashFlow = annualCashFlow,
                pricePerSqft = pricePerSqft(propertyPrice, sizeSqft),
                simpleRoiPercentage = simpleRoiPercentage(
                    annualCashFlow = annualCashFlow,
                    propertyPrice = propertyPrice,
                    appreciationRatePercentage = input.appreciationRatePercentage,
                    initialInvestment = input.initialInvestment,
                ),
            ),
        )
    }

    private fun grossRentalYield(annualRent: Money, propertyPrice: Money): RentalYield {
        return RentalYield((annualRent.amount / propertyPrice.amount) * PERCENT_MULTIPLIER)
    }

    private fun netRentalYield(annualRent: Money, annualCosts: Money, propertyPrice: Money): RentalYield {
        return RentalYield(((annualRent.amount - annualCosts.amount) / propertyPrice.amount) * PERCENT_MULTIPLIER)
    }

    private fun monthlyRent(annualRent: Money): Money = annualRent / MONTHS_IN_YEAR

    private fun monthlyCosts(annualCosts: Money): Money = annualCosts / MONTHS_IN_YEAR

    private fun annualCashFlow(
        annualRent: Money,
        annualCosts: Money,
        annualMortgagePayments: Double = 0.0,
    ): Money {
        return annualRent - annualCosts - Money(annualMortgagePayments, annualRent.currency)
    }

    private fun pricePerSqft(propertyPrice: Money, sizeSqft: Double): Money {
        return Money(propertyPrice.amount / sizeSqft, propertyPrice.currency)
    }

    private fun simpleRoiPercentage(
        annualCashFlow: Money,
        propertyPrice: Money,
        appreciationRatePercentage: Double?,
        initialInvestment: Money?,
    ): Double? {
        if (appreciationRatePercentage == null) return null

        val investmentBase = initialInvestment ?: propertyPrice
        val estimatedAnnualAppreciation = propertyPrice.amount * (appreciationRatePercentage / PERCENT_MULTIPLIER)
        return ((annualCashFlow.amount + estimatedAnnualAppreciation) / investmentBase.amount) * PERCENT_MULTIPLIER
    }

    private fun InvestmentCalculationInput.validate(): List<InvestmentCalculationError> {
        val errors = mutableListOf<InvestmentCalculationError>()

        when {
            propertyPrice == null -> errors += InvestmentCalculationError.MissingPropertyPrice
            !propertyPrice.isPositive() -> errors += InvestmentCalculationError.InvalidPropertyPrice
        }

        when {
            annualRent == null -> errors += InvestmentCalculationError.MissingAnnualRent
            !annualRent.isZeroOrPositive() -> errors += InvestmentCalculationError.InvalidAnnualRent
        }

        annualCosts?.let {
            if (!it.isZeroOrPositive()) {
                errors += InvestmentCalculationError.InvalidAnnualCosts
            }
        }

        when {
            sizeSqft == null -> errors += InvestmentCalculationError.MissingSizeSqft
            sizeSqft <= 0.0 || !sizeSqft.isFinite() -> errors += InvestmentCalculationError.InvalidSizeSqft
        }

        monthlyMortgagePayment?.let {
            if (!it.isZeroOrPositive()) {
                errors += InvestmentCalculationError.InvalidMonthlyMortgagePayment
            }
        }

        appreciationRatePercentage?.let {
            if (!it.isFinite()) {
                errors += InvestmentCalculationError.InvalidAppreciationRate
            }
        }

        initialInvestment?.let {
            if (!it.isPositive()) {
                errors += InvestmentCalculationError.InvalidInitialInvestment
            }
        }

        if (!hasCompatibleCurrencies()) {
            errors += InvestmentCalculationError.CurrencyMismatch
        }

        return errors.distinct()
    }

    private fun InvestmentCalculationInput.hasCompatibleCurrencies(): Boolean {
        val currencies = listOfNotNull(
            propertyPrice,
            annualRent,
            annualCosts,
            monthlyMortgagePayment,
            initialInvestment,
        ).map { it.currency }

        return currencies.distinct().size <= 1
    }

    private const val MONTHS_IN_YEAR = 12.0
    private const val PERCENT_MULTIPLIER = 100.0
}

data class InvestmentCalculationInput(
    val propertyPrice: Money?,
    val annualRent: Money?,
    val annualCosts: Money? = null,
    val sizeSqft: Double?,
    val monthlyMortgagePayment: Money? = null,
    val appreciationRatePercentage: Double? = null,
    val initialInvestment: Money? = null,
)

sealed interface InvestmentCalculationResult {
    data class Complete(
        val metrics: InvestmentMetrics,
    ) : InvestmentCalculationResult

    data class Incomplete(
        val errors: List<InvestmentCalculationError>,
    ) : InvestmentCalculationResult
}

enum class InvestmentCalculationError {
    MissingPropertyPrice,
    InvalidPropertyPrice,
    MissingAnnualRent,
    InvalidAnnualRent,
    InvalidAnnualCosts,
    MissingSizeSqft,
    InvalidSizeSqft,
    InvalidMonthlyMortgagePayment,
    InvalidAppreciationRate,
    InvalidInitialInvestment,
    CurrencyMismatch,
}
