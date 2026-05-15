package amin.codelabs.realestateinvestmentintelligence.dashboard.presentation

import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppChip
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppDivider
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppEmptyState
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppErrorState
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppInfoCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppLoadingIndicator
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppMetricAccent
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppOutlinedButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppPrimaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppSectionTitle
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTertiaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.math.round

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onEvent: (DashboardUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        DashboardUiState.Loading -> DashboardLoadingState(modifier = modifier)
        DashboardUiState.Empty -> DashboardEmptyState(
            modifier = modifier,
            onRetryClick = { onEvent(DashboardUiEvent.RetryClicked) },
        )

        is DashboardUiState.Error -> DashboardErrorState(
            message = state.message,
            modifier = modifier,
            onRetryClick = { onEvent(DashboardUiEvent.RetryClicked) },
        )

        is DashboardUiState.Content -> DashboardContentState(
            content = state.content,
            modifier = modifier,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun DashboardContentState(
    content: DashboardContent,
    onEvent: (DashboardUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding()
            .padding(MaterialTheme.appSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xl),
    ) {
        item {
            DashboardHeader()
        }
        item {
            MarketOverviewSection(overview = content.marketOverview)
        }
        item {
            WatchlistSummarySection(
                summary = content.watchlistSummary,
                onWatchlistClick = { onEvent(DashboardUiEvent.WatchlistClicked) },
            )
        }
        item {
            TopAreasSection(
                areas = content.topInvestmentAreas,
                onAreaClick = { areaId -> onEvent(DashboardUiEvent.AreaClicked(areaId)) },
            )
        }
        item {
            TopOpportunitiesSection(
                properties = content.topPropertyOpportunities,
                onPropertyClick = { propertyId -> onEvent(DashboardUiEvent.PropertyClicked(propertyId)) },
            )
        }
    }
}

@Composable
private fun DashboardHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
        Text(
            text = "Investment dashboard",
            style = MaterialTheme.appTypography.screenTitle,
            color = MaterialTheme.appColors.onBackground,
        )
        Text(
            text = "Current Dubai market signals based on available data.",
            style = MaterialTheme.appTypography.bodySmall,
            color = MaterialTheme.appColors.mutedText,
        )
    }
}

@Composable
private fun MarketOverviewSection(overview: DashboardMarketOverviewUi) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Market overview",
            subtitle = "AED market snapshot",
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Areas tracked",
                    value = overview.areaCount.toString(),
                    subtitle = "Dubai",
                    accent = AppMetricAccent.Neutral,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Opportunities",
                    value = overview.propertyCount.toString(),
                    subtitle = overview.currency,
                    accent = AppMetricAccent.Neutral,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Avg yield",
                    value = overview.averageRentalYieldPercentage.asPercentOrDash(),
                    subtitle = "annual",
                    accent = AppMetricAccent.Positive,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Avg score",
                    value = overview.averageInvestmentScore.asScoreOrDash(),
                    subtitle = "/100",
                    accent = AppMetricAccent.Neutral,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
    }
}

@Composable
private fun WatchlistSummarySection(
    summary: DashboardWatchlistSummaryUi,
    onWatchlistClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Watchlist",
            subtitle = "Saved areas and properties",
            action = {
                AppTertiaryButton(
                    text = "Open",
                    onClick = onWatchlistClick,
                )
            },
        )
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onWatchlistClick),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.lg),
            ) {
                WatchlistMetric(
                    label = "Areas",
                    value = summary.savedAreaCount.toString(),
                    modifier = Modifier.weight(1f),
                )
                WatchlistMetric(
                    label = "Properties",
                    value = summary.savedPropertyCount.toString(),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun WatchlistMetric(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs),
    ) {
        Text(
            text = label,
            style = MaterialTheme.appTypography.metricLabel,
            color = MaterialTheme.appColors.mutedText,
        )
        Text(
            text = value,
            style = MaterialTheme.appTypography.metricValue,
            color = MaterialTheme.appColors.onSurface,
        )
    }
}

@Composable
private fun TopAreasSection(
    areas: List<DashboardAreaItemUi>,
    onAreaClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Top investment areas",
            subtitle = "Ranked by score and yield",
        )
        if (areas.isEmpty()) {
            SectionEmptyMessage(message = "No area signals available yet.")
        } else {
            areas.forEach { area ->
                AreaOpportunityCard(
                    area = area,
                    onClick = { onAreaClick(area.id) },
                )
            }
        }
    }
}

@Composable
private fun AreaOpportunityCard(
    area: DashboardAreaItemUi,
    onClick: () -> Unit,
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs),
                ) {
                    Text(
                        text = area.name,
                        style = MaterialTheme.appTypography.sectionTitle,
                        color = MaterialTheme.appColors.onSurface,
                    )
                    Text(
                        text = "Average yield ${area.averageRentalYieldPercentage.asPercentOrDash()}",
                        style = MaterialTheme.appTypography.bodySmall,
                        color = MaterialTheme.appColors.mutedText,
                    )
                }
                Text(
                    text = area.investmentScore.asScoreOrDash(),
                    style = MaterialTheme.appTypography.metricValue,
                    color = MaterialTheme.appColors.neutralMetric,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                area.demandLabel?.let { label ->
                    AppChip(text = "Demand $label")
                }
                area.riskLabel?.let { label ->
                    AppChip(text = "Risk $label")
                }
            }
        }
    }
}

@Composable
private fun TopOpportunitiesSection(
    properties: List<DashboardPropertyItemUi>,
    onPropertyClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Top opportunities",
            subtitle = "Ranked by yield and investment score",
        )
        if (properties.isEmpty()) {
            SectionEmptyMessage(message = "No property opportunities available yet.")
        } else {
            properties.forEach { property ->
                PropertyOpportunityCard(
                    property = property,
                    onClick = { onPropertyClick(property.id) },
                )
            }
        }
    }
}

@Composable
private fun PropertyOpportunityCard(
    property: DashboardPropertyItemUi,
    onClick: () -> Unit,
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs),
                ) {
                    Text(
                        text = property.title,
                        style = MaterialTheme.appTypography.sectionTitle,
                        color = MaterialTheme.appColors.onSurface,
                    )
                    Text(
                        text = property.priceAmount.asCompactMoney(property.currency),
                        style = MaterialTheme.appTypography.bodySmall,
                        color = MaterialTheme.appColors.mutedText,
                    )
                }
                Text(
                    text = property.netRentalYieldPercentage.asPercentOrDash(),
                    style = MaterialTheme.appTypography.metricValue,
                    color = MaterialTheme.appColors.positive,
                )
            }
            AppDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                PropertyMetric(
                    label = "Gross yield",
                    value = property.grossRentalYieldPercentage.asPercentOrDash(),
                )
                PropertyMetric(
                    label = "Score",
                    value = property.investmentScore.asScoreOrDash(),
                )
            }
        }
    }
}

@Composable
private fun PropertyMetric(
    label: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs)) {
        Text(
            text = label,
            style = MaterialTheme.appTypography.caption,
            color = MaterialTheme.appColors.mutedText,
        )
        Text(
            text = value,
            style = MaterialTheme.appTypography.label,
            color = MaterialTheme.appColors.onSurface,
        )
    }
}

@Composable
private fun MetricGrid(
    first: @Composable () -> Unit,
    second: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
    ) {
        Box(modifier = Modifier.weight(1f)) {
            first()
        }
        Box(modifier = Modifier.weight(1f)) {
            second()
        }
    }
}

@Composable
private fun SectionEmptyMessage(message: String) {
    Text(
        text = message,
        style = MaterialTheme.appTypography.bodySmall,
        color = MaterialTheme.appColors.mutedText,
    )
}

@Composable
private fun DashboardLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding(),
        contentAlignment = Alignment.Center,
    ) {
        AppLoadingIndicator(label = "Loading dashboard")
    }
}

@Composable
private fun DashboardEmptyState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DashboardStateScaffold(modifier = modifier) {
        AppEmptyState(
            title = "No dashboard data yet",
            message = "Market signals will appear here when area and property data is available.",
            action = {
                AppOutlinedButton(
                    text = "Retry",
                    onClick = onRetryClick,
                )
            },
        )
    }
}

@Composable
private fun DashboardErrorState(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DashboardStateScaffold(modifier = modifier) {
        AppErrorState(
            title = "Dashboard unavailable",
            message = message,
            action = {
                AppPrimaryButton(
                    text = "Retry",
                    onClick = onRetryClick,
                )
            },
        )
    }
}

@Composable
private fun DashboardStateScaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding()
            .padding(MaterialTheme.appSpacing.screen),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

private fun Double?.asPercentOrDash(): String = this?.let { "${it.toOneDecimal()}%" } ?: "-"

private fun Double?.asScoreOrDash(): String = this?.let { round(it).toInt().toString() } ?: "-"

private fun Int?.asScoreOrDash(): String = this?.toString() ?: "-"

private fun Double.asCompactMoney(currency: String): String {
    val amount = when {
        this >= 1_000_000.0 -> "${(this / 1_000_000.0).toOneDecimal()}M"
        this >= 1_000.0 -> "${round(this / 1_000.0).toInt()}K"
        else -> round(this).toInt().toString()
    }
    return "$currency $amount"
}

private fun Double.toOneDecimal(): String {
    val rounded = round(this * 10.0) / 10.0
    return if (rounded == round(rounded)) {
        round(rounded).toInt().toString()
    } else {
        rounded.toString()
    }
}
