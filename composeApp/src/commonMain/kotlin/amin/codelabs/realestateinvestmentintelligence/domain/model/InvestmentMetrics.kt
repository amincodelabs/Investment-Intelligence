package amin.codelabs.realestateinvestmentintelligence.domain.model

data class InvestmentMetrics(
    val grossRentalYield: RentalYield,
    val netRentalYield: RentalYield,
    val monthlyRent: Money,
    val monthlyCosts: Money,
    val monthlyCashFlow: Money,
    val annualCashFlow: Money,
    val pricePerSqft: Money,
    val simpleRoiPercentage: Double?,
)
