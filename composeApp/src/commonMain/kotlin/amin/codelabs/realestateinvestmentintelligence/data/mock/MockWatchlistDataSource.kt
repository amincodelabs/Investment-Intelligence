package amin.codelabs.realestateinvestmentintelligence.data.mock

class MockWatchlistDataSource {
    private val savedPropertyIds = linkedSetOf<String>()
    private val savedAreaIds = linkedSetOf<String>()

    fun getSavedPropertyIds(): List<String> = savedPropertyIds.toList()

    fun getSavedAreaIds(): List<String> = savedAreaIds.toList()

    fun containsProperty(propertyId: String): Boolean = propertyId in savedPropertyIds

    fun containsArea(areaId: String): Boolean = areaId in savedAreaIds

    fun saveProperty(propertyId: String) {
        savedPropertyIds += propertyId
    }

    fun removeProperty(propertyId: String): Boolean = savedPropertyIds.remove(propertyId)

    fun saveArea(areaId: String) {
        savedAreaIds += areaId
    }

    fun removeArea(areaId: String): Boolean = savedAreaIds.remove(areaId)
}
