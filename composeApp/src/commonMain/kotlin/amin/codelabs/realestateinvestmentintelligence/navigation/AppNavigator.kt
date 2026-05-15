package amin.codelabs.realestateinvestmentintelligence.navigation

import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthStatus
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository

class AppNavigationCoordinator(
    private val authRepository: AuthRepository,
    private val logger: NavigationLogger = NoOpNavigationLogger,
) {
    fun resolveStartupRoute(): AppRoute {
        val route = when (authRepository.getAuthStatus()) {
            AuthStatus.Authenticated -> AppRoute.Main.Dashboard
            AuthStatus.SignedOut -> AppRoute.Auth.Login
        }

        logger.log(NavigationLogEvent.StartupRouteResolved)
        return route
    }

    fun routeAfterLogout(): AppRoute {
        authRepository.logout()
        logger.log(NavigationLogEvent.SignedOutRouteSelected)
        return AppRoute.Auth.Login
    }

    fun routeAfterAuthenticated(): AppRoute {
        logger.log(NavigationLogEvent.AuthenticatedRouteSelected)
        return AppRoute.Main.Dashboard
    }
}

interface NavigationLogger {
    fun log(event: NavigationLogEvent)
}

enum class NavigationLogEvent {
    StartupRouteResolved,
    AuthenticatedRouteSelected,
    SignedOutRouteSelected,
    RouteChanged,
}

object NoOpNavigationLogger : NavigationLogger {
    override fun log(event: NavigationLogEvent) = Unit
}
