package amin.codelabs.realestateinvestmentintelligence.data.mock

import amin.codelabs.realestateinvestmentintelligence.domain.model.CompletionStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.DemandLevel
import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentMetrics
import amin.codelabs.realestateinvestmentintelligence.domain.model.InvestmentScore
import amin.codelabs.realestateinvestmentintelligence.domain.model.Money
import amin.codelabs.realestateinvestmentintelligence.domain.model.Property
import amin.codelabs.realestateinvestmentintelligence.domain.model.PropertyType
import amin.codelabs.realestateinvestmentintelligence.domain.model.RiskLevel
import amin.codelabs.realestateinvestmentintelligence.domain.service.InvestmentCalculationInput
import amin.codelabs.realestateinvestmentintelligence.domain.service.InvestmentCalculationResult
import amin.codelabs.realestateinvestmentintelligence.domain.service.InvestmentCalculator

class MockPropertyDataSource {
    fun getAllProperties(): List<Property> = properties

    fun getPropertyById(id: String): Property? = properties.firstOrNull { it.id == id }

    fun getPropertiesByAreaId(areaId: String): List<Property> = properties.filter { it.areaId == areaId }

    companion object {
        private val properties = listOf(
            property(
                id = "property-marina-one-bedroom",
                title = "One-bedroom apartment near Marina Walk",
                areaId = MockAreaDataSource.DUBAI_MARINA_ID,
                price = 1_850_000.0,
                sizeSqft = 850.0,
                annualRent = 145_000.0,
                annualCosts = 28_000.0,
                propertyType = PropertyType.Apartment,
                completionStatus = CompletionStatus.Ready,
                riskLevel = RiskLevel.Medium,
                demandLevel = DemandLevel.High,
                score = 82,
                appreciationRatePercentage = 4.8,
            ),
            property(
                id = "property-jvc-studio",
                title = "High-yield studio in JVC",
                areaId = MockAreaDataSource.JVC_ID,
                price = 720_000.0,
                sizeSqft = 430.0,
                annualRent = 62_000.0,
                annualCosts = 9_500.0,
                propertyType = PropertyType.Studio,
                completionStatus = CompletionStatus.Ready,
                riskLevel = RiskLevel.Medium,
                demandLevel = DemandLevel.High,
                score = 87,
                appreciationRatePercentage = 5.3,
            ),
            property(
                id = "property-downtown-two-bedroom",
                title = "Two-bedroom apartment in Downtown Dubai",
                areaId = MockAreaDataSource.DOWNTOWN_DUBAI_ID,
                price = 3_200_000.0,
                sizeSqft = 1_250.0,
                annualRent = 210_000.0,
                annualCosts = 43_000.0,
                propertyType = PropertyType.Apartment,
                completionStatus = CompletionStatus.Ready,
                riskLevel = RiskLevel.Low,
                demandLevel = DemandLevel.High,
                score = 78,
                appreciationRatePercentage = 4.2,
            ),
            property(
                id = "property-business-bay-one-bedroom",
                title = "Canal-view apartment in Business Bay",
                areaId = MockAreaDataSource.BUSINESS_BAY_ID,
                price = 1_450_000.0,
                sizeSqft = 760.0,
                annualRent = 112_000.0,
                annualCosts = 21_000.0,
                propertyType = PropertyType.Apartment,
                completionStatus = CompletionStatus.Ready,
                riskLevel = RiskLevel.Medium,
                demandLevel = DemandLevel.High,
                score = 84,
                appreciationRatePercentage = 5.0,
            ),
            property(
                id = "property-dubai-hills-townhouse",
                title = "Family townhouse in Dubai Hills Estate",
                areaId = MockAreaDataSource.DUBAI_HILLS_ID,
                price = 3_850_000.0,
                sizeSqft = 2_250.0,
                annualRent = 245_000.0,
                annualCosts = 38_000.0,
                propertyType = PropertyType.Townhouse,
                completionStatus = CompletionStatus.Ready,
                riskLevel = RiskLevel.Low,
                demandLevel = DemandLevel.Medium,
                score = 80,
                appreciationRatePercentage = 5.6,
            ),
            property(
                id = "property-palm-jumeirah-apartment",
                title = "Sea-view apartment on Palm Jumeirah",
                areaId = MockAreaDataSource.PALM_JUMEIRAH_ID,
                price = 5_200_000.0,
                sizeSqft = 1_620.0,
                annualRent = 310_000.0,
                annualCosts = 72_000.0,
                propertyType = PropertyType.Apartment,
                completionStatus = CompletionStatus.Ready,
                riskLevel = RiskLevel.Medium,
                demandLevel = DemandLevel.High,
                score = 75,
                appreciationRatePercentage = 3.8,
            ),
        )

        private fun property(
            id: String,
            title: String,
            areaId: String,
            price: Double,
            sizeSqft: Double,
            annualRent: Double,
            annualCosts: Double,
            propertyType: PropertyType,
            completionStatus: CompletionStatus,
            riskLevel: RiskLevel,
            demandLevel: DemandLevel,
            score: Int,
            appreciationRatePercentage: Double,
        ): Property {
            val priceMoney = Money(price)
            val annualRentMoney = Money(annualRent)
            val annualCostsMoney = Money(annualCosts)

            return Property(
                id = id,
                title = title,
                areaId = areaId,
                price = priceMoney,
                sizeSqft = sizeSqft,
                expectedAnnualRent = annualRentMoney,
                annualCosts = annualCostsMoney,
                propertyType = propertyType,
                completionStatus = completionStatus,
                riskLevel = riskLevel,
                demandLevel = demandLevel,
                investmentScore = InvestmentScore.from(score),
                investmentMetrics = investmentMetrics(
                    propertyPrice = priceMoney,
                    annualRent = annualRentMoney,
                    annualCosts = annualCostsMoney,
                    sizeSqft = sizeSqft,
                    appreciationRatePercentage = appreciationRatePercentage,
                ),
            )
        }

        private fun investmentMetrics(
            propertyPrice: Money,
            annualRent: Money,
            annualCosts: Money,
            sizeSqft: Double,
            appreciationRatePercentage: Double,
        ): InvestmentMetrics {
            val result = InvestmentCalculator.calculate(
                InvestmentCalculationInput(
                    propertyPrice = propertyPrice,
                    annualRent = annualRent,
                    annualCosts = annualCosts,
                    sizeSqft = sizeSqft,
                    appreciationRatePercentage = appreciationRatePercentage,
                ),
            )

            return when (result) {
                is InvestmentCalculationResult.Complete -> result.metrics
                is InvestmentCalculationResult.Incomplete -> error("Invalid mock property investment inputs.")
            }
        }
    }
}
