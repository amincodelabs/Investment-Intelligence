package amin.codelabs.realestateinvestmentintelligence.data.mock

import amin.codelabs.realestateinvestmentintelligence.domain.model.Area
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentScore
import amin.codelabs.realestateinvestmentintelligence.domain.model.Money
import amin.codelabs.realestateinvestmentintelligence.domain.model.RentalYield
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel

class MockAreaDataSource {
    fun getAllAreas(): List<Area> = areas

    fun getAreaById(id: String): Area? = areas.firstOrNull { it.id == id }

    companion object {
        const val DUBAI_MARINA_ID = "area-dubai-marina"
        const val JVC_ID = "area-jvc"
        const val DOWNTOWN_DUBAI_ID = "area-downtown-dubai"
        const val BUSINESS_BAY_ID = "area-business-bay"
        const val DUBAI_HILLS_ID = "area-dubai-hills"
        const val PALM_JUMEIRAH_ID = "area-palm-jumeirah"

        private val areas = listOf(
            Area(
                id = DUBAI_MARINA_ID,
                name = "Dubai Marina",
                averagePropertyPrice = Money(1_850_000.0),
                averageRentalYield = RentalYield(6.9),
                demandLevel = DemandLevel.High,
                riskLevel = RiskLevel.Medium,
                appreciationPotentialPercentage = 4.8,
                investmentScore = InvestmentScore.from(82),
            ),
            Area(
                id = JVC_ID,
                name = "Jumeirah Village Circle",
                averagePropertyPrice = Money(950_000.0),
                averageRentalYield = RentalYield(7.6),
                demandLevel = DemandLevel.High,
                riskLevel = RiskLevel.Medium,
                appreciationPotentialPercentage = 5.3,
                investmentScore = InvestmentScore.from(86),
            ),
            Area(
                id = DOWNTOWN_DUBAI_ID,
                name = "Downtown Dubai",
                averagePropertyPrice = Money(2_650_000.0),
                averageRentalYield = RentalYield(5.5),
                demandLevel = DemandLevel.High,
                riskLevel = RiskLevel.Low,
                appreciationPotentialPercentage = 4.2,
                investmentScore = InvestmentScore.from(78),
            ),
            Area(
                id = BUSINESS_BAY_ID,
                name = "Business Bay",
                averagePropertyPrice = Money(1_450_000.0),
                averageRentalYield = RentalYield(6.8),
                demandLevel = DemandLevel.High,
                riskLevel = RiskLevel.Medium,
                appreciationPotentialPercentage = 5.0,
                investmentScore = InvestmentScore.from(83),
            ),
            Area(
                id = DUBAI_HILLS_ID,
                name = "Dubai Hills Estate",
                averagePropertyPrice = Money(2_250_000.0),
                averageRentalYield = RentalYield(5.9),
                demandLevel = DemandLevel.Medium,
                riskLevel = RiskLevel.Low,
                appreciationPotentialPercentage = 5.6,
                investmentScore = InvestmentScore.from(80),
            ),
            Area(
                id = PALM_JUMEIRAH_ID,
                name = "Palm Jumeirah",
                averagePropertyPrice = Money(4_800_000.0),
                averageRentalYield = RentalYield(4.9),
                demandLevel = DemandLevel.High,
                riskLevel = RiskLevel.Medium,
                appreciationPotentialPercentage = 3.8,
                investmentScore = InvestmentScore.from(74),
            ),
        )
    }
}
