package amin.codelabs.realestateinvestmentintelligence.navigation

sealed interface AppRoute {
    data object Splash : AppRoute

    sealed interface Auth : AppRoute {
        data object Login : Auth
        data object Register : Auth
    }

    sealed interface Main : AppRoute {
        data object Dashboard : Main
        data object Areas : Main
        data class AreaDetails(
            val areaId: String,
        ) : Main

        data class PropertyDetails(
            val propertyId: String,
        ) : Main

        data object Calculator : Main
        data object Comparison : Main
        data object Watchlist : Main
        data object ProfileSettings : Main
    }
}

val MainTabRoutes = listOf(
    AppRoute.Main.Dashboard,
    AppRoute.Main.Areas,
    AppRoute.Main.Calculator,
    AppRoute.Main.Watchlist,
    AppRoute.Main.ProfileSettings,
)

fun AppRoute.label(): String = when (this) {
    AppRoute.Splash -> "Startup"
    AppRoute.Auth.Login -> "Login"
    AppRoute.Auth.Register -> "Register"
    AppRoute.Main.Dashboard -> "Dashboard"
    AppRoute.Main.Areas -> "Areas"
    is AppRoute.Main.AreaDetails -> "Area Details"
    is AppRoute.Main.PropertyDetails -> "Property Details"
    AppRoute.Main.Calculator -> "Calculator"
    AppRoute.Main.Comparison -> "Comparison"
    AppRoute.Main.Watchlist -> "Watchlist"
    AppRoute.Main.ProfileSettings -> "Profile"
}
