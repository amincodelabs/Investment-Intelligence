package amin.codelabs.realestateinvestmentintelligence.domain.model

data class Property(
    val id: String,
    val title: String,
    val areaId: String,
    val price: Money,
    val sizeSqft: Double,
    val expectedAnnualRent: Money?,
    val annualCosts: Money?,
    val propertyType: PropertyType,
    val completionStatus: CompletionStatus,
    val riskLevel: RiskLevel? = null,
    val demandLevel: DemandLevel? = null,
    val investmentScore: InvestmentScore? = null,
    val investmentMetrics: InvestmentMetrics? = null,
)
