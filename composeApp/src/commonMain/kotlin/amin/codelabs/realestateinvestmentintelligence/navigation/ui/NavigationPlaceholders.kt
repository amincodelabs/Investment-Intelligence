package amin.codelabs.realestateinvestmentintelligence.navigation.ui

import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppDivider
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppLoadingIndicator
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppOutlinedButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppPrimaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppSecondaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTertiaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTopBar
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import amin.codelabs.realestateinvestmentintelligence.navigation.AppRoute
import amin.codelabs.realestateinvestmentintelligence.navigation.MainTabRoutes
import amin.codelabs.realestateinvestmentintelligence.navigation.label
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashPlaceholderScreen() {
    AppScreenScaffold {
        AppLoadingIndicator(label = "Preparing app")
    }
}

@Composable
fun MainPlaceholderScreen(
    route: AppRoute.Main,
    onTabSelected: (AppRoute.Main) -> Unit,
    onAreaDetailsClick: () -> Unit,
    onPropertyDetailsClick: () -> Unit,
    onComparisonClick: () -> Unit,
    onBackToAreasClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    AppScreenScaffold {
        AppTopBar(
            title = route.label(),
            actions = {
                AppTertiaryButton(
                    text = "Logout",
                    onClick = onLogoutClick,
                )
            },
        )
        AppDivider()
        MainTabs(
            selectedRoute = route,
            onTabSelected = onTabSelected,
        )
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
                Text(
                    text = route.label(),
                    style = MaterialTheme.appTypography.sectionTitle,
                    color = MaterialTheme.appColors.onSurface,
                )
                Text(
                    text = "Placeholder for the ${route.label()} feature.",
                    style = MaterialTheme.appTypography.body,
                    color = MaterialTheme.appColors.mutedText,
                )
                RouteActions(
                    route = route,
                    onAreaDetailsClick = onAreaDetailsClick,
                    onPropertyDetailsClick = onPropertyDetailsClick,
                    onComparisonClick = onComparisonClick,
                    onBackToAreasClick = onBackToAreasClick,
                )
            }
        }
    }
}

@Composable
private fun MainTabs(
    selectedRoute: AppRoute.Main,
    onTabSelected: (AppRoute.Main) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm),
    ) {
        MainTabRoutes.forEach { route ->
            val isSelected = selectedRoute::class == route::class
            if (isSelected) {
                AppSecondaryButton(
                    text = route.label(),
                    onClick = { onTabSelected(route) },
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                AppOutlinedButton(
                    text = route.label(),
                    onClick = { onTabSelected(route) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun RouteActions(
    route: AppRoute.Main,
    onAreaDetailsClick: () -> Unit,
    onPropertyDetailsClick: () -> Unit,
    onComparisonClick: () -> Unit,
    onBackToAreasClick: () -> Unit,
) {
    when (route) {
        AppRoute.Main.Dashboard -> Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
            AppPrimaryButton(
                text = "Open areas",
                onClick = onBackToAreasClick,
                modifier = Modifier.fillMaxWidth(),
            )
            AppOutlinedButton(
                text = "Open property details",
                onClick = onPropertyDetailsClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        AppRoute.Main.Areas -> AppPrimaryButton(
            text = "Open area details",
            onClick = onAreaDetailsClick,
            modifier = Modifier.fillMaxWidth(),
        )

        is AppRoute.Main.AreaDetails -> AppOutlinedButton(
            text = "Back to areas",
            onClick = onBackToAreasClick,
            modifier = Modifier.fillMaxWidth(),
        )

        is AppRoute.Main.PropertyDetails -> AppOutlinedButton(
            text = "Add to comparison",
            onClick = onComparisonClick,
            modifier = Modifier.fillMaxWidth(),
        )

        AppRoute.Main.Calculator,
        AppRoute.Main.Comparison,
        AppRoute.Main.Watchlist,
        AppRoute.Main.ProfileSettings,
        -> Text(
            text = "Feature actions will be added with the dedicated screen task.",
            style = MaterialTheme.appTypography.bodySmall,
            color = MaterialTheme.appColors.mutedText,
        )
    }
}

@Composable
private fun AppScreenScaffold(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding()
            .padding(MaterialTheme.appSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.lg),
    ) {
        content()
    }
}
