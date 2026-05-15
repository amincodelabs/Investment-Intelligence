package amin.codelabs.realestateinvestmentintelligence.navigation

import amin.codelabs.realestateinvestmentintelligence.auth.presentation.LoginRoute
import amin.codelabs.realestateinvestmentintelligence.auth.presentation.RegisterRoute
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.navigation.ui.MainPlaceholderScreen
import amin.codelabs.realestateinvestmentintelligence.navigation.ui.SplashPlaceholderScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun AppNavigationRoot(
    authRepository: AuthRepository,
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

        is AppRoute.Main -> MainPlaceholderScreen(
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
