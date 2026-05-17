package amin.codelabs.realestateinvestmentintelligence.navigation

import amin.codelabs.realestateinvestmentintelligence.area.presentation.AreaIntelligenceRoute
import amin.codelabs.realestateinvestmentintelligence.auth.presentation.LoginRoute
import amin.codelabs.realestateinvestmentintelligence.auth.presentation.RegisterRoute
import amin.codelabs.realestateinvestmentintelligence.calculator.presentation.InvestmentCalculatorRoute
import amin.codelabs.realestateinvestmentintelligence.dashboard.presentation.DashboardRoute
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AreaRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.PropertyRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.WatchlistRepository
import amin.codelabs.realestateinvestmentintelligence.navigation.ui.MainPlaceholderScreen
import amin.codelabs.realestateinvestmentintelligence.navigation.ui.SplashPlaceholderScreen
import amin.codelabs.realestateinvestmentintelligence.property.presentation.PropertyDetailsRoute
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AppNavigationRoot(
    authRepository: AuthRepository,
    areaRepository: AreaRepository,
    propertyRepository: PropertyRepository,
    watchlistRepository: WatchlistRepository,
    logger: NavigationLogger = NoOpNavigationLogger,
) {
    val coordinator = remember(authRepository, logger) {
        AppNavigationCoordinator(
            authRepository = authRepository,
            logger = logger,
        )
    }
    var currentRoute by remember { mutableStateOf<AppRoute>(AppRoute.Splash) }

    fun navigate(route: AppRoute) {
        currentRoute = route
        logger.log(NavigationLogEvent.RouteChanged)
    }

    when (val route = currentRoute) {
        AppRoute.Splash -> {
            SplashPlaceholderScreen()
            LaunchedEffect(Unit) {
                navigate(coordinator.resolveStartupRoute())
            }
        }

        AppRoute.Auth.Login -> LoginRoute(
            authRepository = authRepository,
            onRegisterClick = { navigate(AppRoute.Auth.Register) },
            onAuthenticated = { navigate(coordinator.routeAfterAuthenticated()) },
        )

        AppRoute.Auth.Register -> RegisterRoute(
            authRepository = authRepository,
            onLoginClick = { navigate(AppRoute.Auth.Login) },
            onAuthenticated = { navigate(coordinator.routeAfterAuthenticated()) },
        )

        AppRoute.Main.Dashboard -> DashboardRoute(
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
            onAreaClick = { areaId -> navigate(AppRoute.Main.AreaDetails(areaId)) },
            onPropertyClick = { propertyId -> navigate(AppRoute.Main.PropertyDetails(propertyId)) },
            onWatchlistClick = { navigate(AppRoute.Main.Watchlist) },
        )

        AppRoute.Main.Areas -> AreaIntelligenceRoute(
            areaId = null,
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
            onAreaClick = { areaId -> navigate(AppRoute.Main.AreaDetails(areaId)) },
            onPropertyClick = { propertyId -> navigate(AppRoute.Main.PropertyDetails(propertyId)) },
            onBackClick = { navigate(AppRoute.Main.Dashboard) },
        )

        is AppRoute.Main.AreaDetails -> AreaIntelligenceRoute(
            areaId = route.areaId,
            areaRepository = areaRepository,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
            onAreaClick = { areaId -> navigate(AppRoute.Main.AreaDetails(areaId)) },
            onPropertyClick = { propertyId -> navigate(AppRoute.Main.PropertyDetails(propertyId)) },
            onBackClick = { navigate(AppRoute.Main.Areas) },
        )

        is AppRoute.Main.PropertyDetails -> PropertyDetailsRoute(
            propertyId = route.propertyId,
            propertyRepository = propertyRepository,
            watchlistRepository = watchlistRepository,
            onAreaClick = { areaId -> navigate(AppRoute.Main.AreaDetails(areaId)) },
            onComparisonClick = { navigate(AppRoute.Main.Comparison) },
            onBackClick = { navigate(AppRoute.Main.Dashboard) },
        )

        AppRoute.Main.Calculator -> InvestmentCalculatorRoute()

        is AppRoute.Main -> {
            MainPlaceholderScreen(
                route = route,
                onTabSelected = ::navigate,
                onAreaDetailsClick = { navigate(AppRoute.Main.AreaDetails("demo-area")) },
                onPropertyDetailsClick = { navigate(AppRoute.Main.PropertyDetails("demo-property")) },
                onComparisonClick = { navigate(AppRoute.Main.Comparison) },
                onBackToAreasClick = { navigate(AppRoute.Main.Areas) },
                onLogoutClick = { navigate(coordinator.routeAfterLogout()) },
            )
        }
    }
}
