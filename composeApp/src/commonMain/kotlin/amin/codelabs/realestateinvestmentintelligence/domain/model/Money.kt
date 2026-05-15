package amin.codelabs.realestateinvestmentintelligence.domain.model

data class Money(
    val amount: Double,
    val currency: String = DEFAULT_CURRENCY,
) {
    operator fun plus(other: Money): Money {
        requireSameCurrency(other)
        return copy(amount = amount + other.amount)
    }

    operator fun minus(other: Money): Money {
        requireSameCurrency(other)
        return copy(amount = amount - other.amount)
    }

    operator fun div(divisor: Double): Money = copy(amount = amount / divisor)

    fun isFinite(): Boolean = amount.isFinite()

    fun isPositive(): Boolean = amount > 0.0 && amount.isFinite()

    fun isZeroOrPositive(): Boolean = amount >= 0.0 && amount.isFinite()

    private fun requireSameCurrency(other: Money) {
        require(currency == other.currency) {
            "Money values must use the same currency."
        }
    }

    companion object {
        const val DEFAULT_CURRENCY = "AED"

        fun zero(currency: String = DEFAULT_CURRENCY): Money = Money(0.0, currency)
    }
}
