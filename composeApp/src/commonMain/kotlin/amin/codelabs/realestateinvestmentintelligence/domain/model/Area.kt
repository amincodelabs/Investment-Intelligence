package amin.codelabs.realestateinvestmentintelligence.domain.model

data class Area(
    val id: String,
    val name: String,
    val averagePropertyPrice: Money? = null,
    val averageRentalYield: RentalYield? = null,
    val demandLevel: DemandLevel? = null,
    val riskLevel: RiskLevel? = null,
    val appreciationPotentialPercentage: Double? = null,
    val investmentScore: InvestmentScore? = null,
)
