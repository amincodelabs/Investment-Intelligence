package amin.codelabs.realestateinvestmentintelligence.navigation

import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthSession
import amin.codelabs.realestateinvestmentintelligence.domain.model.AuthStatus
import amin.codelabs.realestateinvestmentintelligence.domain.model.LoginCredentials
import amin.codelabs.realestateinvestmentintelligence.domain.model.RegistrationData
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthRepository
import amin.codelabs.realestateinvestmentintelligence.domain.repository.AuthResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppNavigationCoordinatorTest {
    @Test
    fun `resolveStartupRoute returns login when user is signed out`() {
        val logger = RecordingNavigationLogger()
        val coordinator = AppNavigationCoordinator(
            authRepository = FakeAuthRepository(AuthStatus.SignedOut),
            logger = logger,
        )

        val route = coordinator.resolveStartupRoute()

        assertEquals(AppRoute.Auth.Login, route)
        assertTrue(NavigationLogEvent.StartupRouteResolved in logger.events)
    }

    @Test
    fun `resolveStartupRoute returns dashboard when user is authenticated`() {
        val coordinator = AppNavigationCoordinator(
            FakeAuthRepository(AuthStatus.Authenticated),
        )

        val route = coordinator.resolveStartupRoute()

        assertEquals(AppRoute.Main.Dashboard, route)
    }

    @Test
    fun `routeAfterLogout clears session and returns login`() {
        val authRepository = FakeAuthRepository(AuthStatus.Authenticated)
        val coordinator = AppNavigationCoordinator(authRepository)

        val route = coordinator.routeAfterLogout()

        assertEquals(AppRoute.Auth.Login, route)
        assertEquals(AppRoute.Auth.Login, coordinator.resolveStartupRoute())
    }

    @Test
    fun `routeAfterAuthenticated returns dashboard`() {
        val coordinator = AppNavigationCoordinator(FakeAuthRepository(AuthStatus.SignedOut))

        assertEquals(AppRoute.Main.Dashboard, coordinator.routeAfterAuthenticated())
    }

    @Test
    fun `main tab routes include expected destinations`() {
        assertEquals(
            listOf(
                AppRoute.Main.Dashboard,
                AppRoute.Main.Areas,
                AppRoute.Main.Calculator,
                AppRoute.Main.Watchlist,
                AppRoute.Main.ProfileSettings,
            ),
            MainTabRoutes,
        )
    }

    private class RecordingNavigationLogger : NavigationLogger {
        val events = mutableListOf<NavigationLogEvent>()

        override fun log(event: NavigationLogEvent) {
            events += event
        }
    }

    private class FakeAuthRepository(
        private var status: AuthStatus,
    ) : AuthRepository {
        override fun register(data: RegistrationData): AuthResult<AuthSession> {
            status = AuthStatus.Authenticated
            return AuthResult.Success(AuthSession(user = null, status = status))
        }

        override fun login(credentials: LoginCredentials): AuthResult<AuthSession> {
            status = AuthStatus.Authenticated
            return AuthResult.Success(AuthSession(user = null, status = status))
        }

        override fun logout(): AuthResult<Unit> {
            status = AuthStatus.SignedOut
            return AuthResult.Success(Unit)
        }

        override fun getCurrentSession(): AuthSession = AuthSession(
            user = null,
            status = status,
        )

        override fun getAuthStatus(): AuthStatus = status
    }
}
