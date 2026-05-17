package amin.codelabs.realestateinvestmentintelligence.area.presentation

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
fun AreaIntelligenceScreen(
    state: AreaIntelligenceUiState,
    detailMode: Boolean,
    onEvent: (AreaIntelligenceUiEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        AreaIntelligenceUiState.Loading -> AreaLoadingState(modifier = modifier)
        AreaIntelligenceUiState.Empty -> AreaEmptyState(
            modifier = modifier,
            onRetryClick = { onEvent(AreaIntelligenceUiEvent.RetryClicked) },
        )

        is AreaIntelligenceUiState.Error -> AreaErrorState(
            message = state.message,
            modifier = modifier,
            onRetryClick = { onEvent(AreaIntelligenceUiEvent.RetryClicked) },
        )

        is AreaIntelligenceUiState.Content -> {
            if (detailMode) {
                AreaDetailsContent(
                    content = state.content,
                    modifier = modifier,
                    onEvent = onEvent,
                    onBackClick = onBackClick,
                )
            } else {
                AreaListContent(
                    areas = state.content.areas,
                    modifier = modifier,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun AreaListContent(
    areas: List<AreaListItemUi>,
    onEvent: (AreaIntelligenceUiEvent) -> Unit,
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
            AreaHeader(
                title = "Area intelligence",
                subtitle = "Compare Dubai investment zones by yield, risk, demand, and score.",
            )
        }
        item {
            AppSectionTitle(
                title = "Investment areas",
                subtitle = "${areas.size} areas tracked",
            )
        }
        areas.forEach { area ->
            item(key = area.id) {
                AreaListCard(
                    area = area,
                    onClick = { onEvent(AreaIntelligenceUiEvent.AreaClicked(area.id)) },
                )
            }
        }
    }
}

@Composable
private fun AreaDetailsContent(
    content: AreaIntelligenceContent,
    onEvent: (AreaIntelligenceUiEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val area = content.selectedArea

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding()
            .padding(MaterialTheme.appSpacing.screen),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xl),
    ) {
        item {
            AppTertiaryButton(
                text = "Back",
                onClick = onBackClick,
            )
        }
        if (area == null) {
            item {
                AppEmptyState(
                    title = "Area not selected",
                    message = "Choose an area from the list to review its investment metrics.",
                    action = {
                        AppOutlinedButton(
                            text = "Back to areas",
                            onClick = onBackClick,
                        )
                    },
                )
            }
        } else {
            item {
                AreaDetailsHeader(
                    area = area,
                    onSaveClick = {
                        if (area.isSaved) {
                            onEvent(AreaIntelligenceUiEvent.RemoveAreaClicked(area.id))
                        } else {
                            onEvent(AreaIntelligenceUiEvent.SaveAreaClicked(area.id))
                        }
                    },
                )
            }
            item {
                AreaMetricsSection(area = area)
            }
            if (content.properties.isNotEmpty()) {
                item {
                    RelatedPropertiesSection(
                        properties = content.properties,
                        onPropertyClick = { propertyId ->
                            onEvent(AreaIntelligenceUiEvent.PropertyClicked(propertyId))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AreaHeader(
    title: String,
    subtitle: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
        Text(
            text = title,
            style = MaterialTheme.appTypography.screenTitle,
            color = MaterialTheme.appColors.onBackground,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.appTypography.bodySmall,
            color = MaterialTheme.appColors.mutedText,
        )
    }
}

@Composable
private fun AreaListCard(
    area: AreaListItemUi,
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
                        text = area.averagePriceAmount.asCompactMoney(area.currency),
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
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                AppChip(text = "Yield ${area.averageRentalYieldPercentage.asPercentOrDash()}")
                Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                    area.demandLabel?.let { AppChip(text = "Demand $it") }
                    area.riskLabel?.let { AppChip(text = "Risk $it") }
                }
            }
        }
    }
}

@Composable
private fun AreaDetailsHeader(
    area: AreaDetailsUi,
    onSaveClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm),
            ) {
                Text(
                    text = area.name,
                    style = MaterialTheme.appTypography.screenTitle,
                    color = MaterialTheme.appColors.onBackground,
                )
                Text(
                    text = "Area-level investment indicators. Metrics are estimates, not guaranteed returns.",
                    style = MaterialTheme.appTypography.bodySmall,
                    color = MaterialTheme.appColors.mutedText,
                )
            }
            AppOutlinedButton(
                text = if (area.isSaved) "Remove" else "Save",
                onClick = onSaveClick,
            )
        }
        AreaSummaryCard(area = area)
    }
}

@Composable
private fun AreaSummaryCard(area: AreaDetailsUi) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
            Text(
                text = "Area summary",
                style = MaterialTheme.appTypography.sectionTitle,
                color = MaterialTheme.appColors.onSurface,
            )
            Text(
                text = buildAreaSummary(area),
                style = MaterialTheme.appTypography.bodySmall,
                color = MaterialTheme.appColors.mutedText,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                area.demandLabel?.let { AppChip(text = "Demand $it") }
                area.riskLabel?.let { AppChip(text = "Risk $it") }
            }
        }
    }
}

@Composable
private fun AreaMetricsSection(area: AreaDetailsUi) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Key investment metrics",
            subtitle = "Yield, price, score, and growth signals",
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Investment score",
                    value = area.investmentScore.asScoreOrDash(),
                    subtitle = "/100",
                    accent = AppMetricAccent.Neutral,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Rental yield",
                    value = area.averageRentalYieldPercentage.asPercentOrDash(),
                    subtitle = "avg annual",
                    accent = AppMetricAccent.Positive,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Avg price",
                    value = area.averagePriceAmount.asCompactMoney(area.currency),
                    subtitle = area.currency,
                    accent = AppMetricAccent.Neutral,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Appreciation",
                    value = area.appreciationPotentialPercentage.asPercentOrDash(),
                    subtitle = "potential",
                    accent = AppMetricAccent.Warning,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
    }
}

@Composable
private fun RelatedPropertiesSection(
    properties: List<AreaPropertyItemUi>,
    onPropertyClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Related properties",
            subtitle = "Opportunities in this area",
        )
        properties.forEach { property ->
            RelatedPropertyCard(
                property = property,
                onClick = { onPropertyClick(property.id) },
            )
        }
    }
}

@Composable
private fun RelatedPropertyCard(
    property: AreaPropertyItemUi,
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
                PropertyMiniMetric(
                    label = "Size",
                    value = "${round(property.sizeSqft).toInt()} sqft",
                )
                PropertyMiniMetric(
                    label = "Score",
                    value = property.investmentScore.asScoreOrDash(),
                )
            }
        }
    }
}

@Composable
private fun PropertyMiniMetric(
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
private fun AreaLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding(),
        contentAlignment = Alignment.Center,
    ) {
        AppLoadingIndicator(label = "Loading area intelligence")
    }
}

@Composable
private fun AreaEmptyState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AreaStateScaffold(modifier = modifier) {
        AppEmptyState(
            title = "No areas available",
            message = "Investment area signals will appear here when data is available.",
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
private fun AreaErrorState(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AreaStateScaffold(modifier = modifier) {
        AppErrorState(
            title = "Area intelligence unavailable",
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
private fun AreaStateScaffold(
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

private fun buildAreaSummary(area: AreaDetailsUi): String {
    val yield = area.averageRentalYieldPercentage.asPercentOrDash()
    val score = area.investmentScore.asScoreOrDash()
    return "${area.name} currently shows an average rental yield of $yield with an investment score of $score."
}

private fun Double?.asPercentOrDash(): String = this?.let { "${it.toOneDecimal()}%" } ?: "-"

private fun Int?.asScoreOrDash(): String = this?.toString() ?: "-"

private fun Double?.asCompactMoney(currency: String): String {
    val value = this ?: return "-"
    val amount = when {
        value >= 1_000_000.0 -> "${(value / 1_000_000.0).toOneDecimal()}M"
        value >= 1_000.0 -> "${round(value / 1_000.0).toInt()}K"
        else -> round(value).toInt().toString()
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
