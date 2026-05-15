package amin.codelabs.realestateinvestmentintelligence.domain.model

data class RentalYield(
    val percentage: Double,
) {
    fun isValid(): Boolean = percentage.isFinite()
}
