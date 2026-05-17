package amin.codelabs.realestateinvestmentintelligence.property.presentation

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
import kotlin.math.abs
import kotlin.math.round

@Composable
fun PropertyDetailsScreen(
    state: PropertyDetailsUiState,
    onEvent: (PropertyDetailsUiEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        PropertyDetailsUiState.Loading -> PropertyDetailsLoadingState(modifier = modifier)
        PropertyDetailsUiState.Empty -> PropertyDetailsEmptyState(
            modifier = modifier,
            onBackClick = onBackClick,
        )

        is PropertyDetailsUiState.Content -> PropertyDetailsContentState(
            property = state.content.property,
            modifier = modifier,
            onEvent = onEvent,
            onBackClick = onBackClick,
        )

        is PropertyDetailsUiState.Error -> PropertyDetailsErrorState(
            message = state.message,
            modifier = modifier,
            onRetryClick = { onEvent(PropertyDetailsUiEvent.RetryClicked) },
        )
    }
}

@Composable
private fun PropertyDetailsContentState(
    property: PropertyDetailsUi,
    onEvent: (PropertyDetailsUiEvent) -> Unit,
    onBackClick: () -> Unit,
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
            AppTertiaryButton(
                text = "Back",
                onClick = onBackClick,
            )
        }
        item {
            PropertyHeader(
                property = property,
                onSaveClick = {
                    if (property.isSaved) {
                        onEvent(PropertyDetailsUiEvent.RemovePropertyClicked(property.id))
                    } else {
                        onEvent(PropertyDetailsUiEvent.SavePropertyClicked(property.id))
                    }
                },
                onComparisonClick = {
                    if (property.isInComparison) {
                        onEvent(PropertyDetailsUiEvent.RemoveFromComparisonClicked(property.id))
                    } else {
                        onEvent(PropertyDetailsUiEvent.AddToComparisonClicked(property.id))
                    }
                },
            )
        }
        item {
            PriceRentYieldSection(property = property)
        }
        item {
            InvestmentScoreSection(property = property)
        }
        item {
            AreaSummarySection(
                property = property,
                onAreaClick = { onEvent(PropertyDetailsUiEvent.AreaClicked(property.areaId)) },
            )
        }
        item {
            CashFlowSection(property = property)
        }
    }
}

@Composable
private fun PropertyHeader(
    property: PropertyDetailsUi,
    onSaveClick: () -> Unit,
    onComparisonClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
            Text(
                text = property.title,
                style = MaterialTheme.appTypography.screenTitle,
                color = MaterialTheme.appColors.onBackground,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                AppChip(text = property.propertyTypeLabel)
                AppChip(text = property.completionStatusLabel)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
        ) {
            AppOutlinedButton(
                text = if (property.isSaved) "Remove" else "Save",
                onClick = onSaveClick,
                modifier = Modifier.weight(1f),
            )
            AppPrimaryButton(
                text = if (property.isInComparison) "Remove compare" else "Compare",
                onClick = onComparisonClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun PriceRentYieldSection(property: PropertyDetailsUi) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Price, rent, yield",
            subtitle = "Core property economics",
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Price",
                    value = property.priceAmount.asCompactMoney(property.currency),
                    subtitle = property.currency,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Annual rent",
                    value = property.expectedAnnualRentAmount.asCompactMoney(property.currency),
                    subtitle = "expected",
                    accent = AppMetricAccent.Positive,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Gross yield",
                    value = property.grossRentalYieldPercentage.asPercentOrDash(),
                    subtitle = "annual",
                    accent = AppMetricAccent.Positive,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Net yield",
                    value = property.netRentalYieldPercentage.asPercentOrDash(),
                    subtitle = "after costs",
                    accent = property.netRentalYieldPercentage.toMetricAccent(),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
    }
}

@Composable
private fun InvestmentScoreSection(property: PropertyDetailsUi) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Investment score",
            subtitle = "Estimated opportunity quality",
        )
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs)) {
                        Text(
                            text = "Overall score",
                            style = MaterialTheme.appTypography.metricLabel,
                            color = MaterialTheme.appColors.mutedText,
                        )
                        Text(
                            text = property.investmentScore.asScoreOrDash(),
                            style = MaterialTheme.appTypography.metricValue,
                            color = MaterialTheme.appColors.neutralMetric,
                        )
                    }
                    Text(
                        text = "/100",
                        style = MaterialTheme.appTypography.caption,
                        color = MaterialTheme.appColors.mutedText,
                    )
                }
                AppDivider()
                Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                    property.demandLabel?.let { AppChip(text = "Demand $it") }
                    property.riskLabel?.let { AppChip(text = "Risk $it") }
                }
                Text(
                    text = "Scores are estimates based on available investment signals and should be reviewed alongside price, yield, and cash flow.",
                    style = MaterialTheme.appTypography.bodySmall,
                    color = MaterialTheme.appColors.mutedText,
                )
            }
        }
    }
}

@Composable
private fun AreaSummarySection(
    property: PropertyDetailsUi,
    onAreaClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Area summary",
            subtitle = "Location context for this asset",
        )
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onAreaClick),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
                Text(
                    text = "Area reference",
                    style = MaterialTheme.appTypography.metricLabel,
                    color = MaterialTheme.appColors.mutedText,
                )
                Text(
                    text = property.areaId,
                    style = MaterialTheme.appTypography.sectionTitle,
                    color = MaterialTheme.appColors.onSurface,
                )
                AppDivider()
                MetricGrid(
                    first = {
                        PropertyMiniMetric(
                            label = "Size",
                            value = "${round(property.sizeSqft).toInt()} sqft",
                        )
                    },
                    second = {
                        PropertyMiniMetric(
                            label = "Price per sqft",
                            value = property.pricePerSqftAmount.asMoneyOrDash(property.currency),
                        )
                    },
                )
                AppTertiaryButton(
                    text = "Open area",
                    onClick = onAreaClick,
                )
            }
        }
    }
}

@Composable
private fun CashFlowSection(property: PropertyDetailsUi) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Cash flow",
            subtitle = "Estimated monthly and annual position",
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Monthly rent",
                    value = property.monthlyRentAmount.asMoneyOrDash(property.currency),
                    subtitle = "expected",
                    accent = AppMetricAccent.Positive,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Monthly costs",
                    value = property.monthlyCostsAmount.asMoneyOrDash(property.currency),
                    subtitle = "estimated",
                    accent = AppMetricAccent.Warning,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Monthly cash flow",
                    value = property.monthlyCashFlowAmount.asSignedMoneyOrDash(property.currency),
                    subtitle = "net",
                    accent = property.monthlyCashFlowAmount.toMetricAccent(),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Annual cash flow",
                    value = property.annualCashFlowAmount.asSignedMoneyOrDash(property.currency),
                    subtitle = "net",
                    accent = property.annualCashFlowAmount.toMetricAccent(),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
        AppInfoCard(
            title = "Simple ROI",
            value = property.simpleRoiPercentage.asPercentOrDash(),
            subtitle = "estimate",
            accent = property.simpleRoiPercentage.toMetricAccent(),
            modifier = Modifier.fillMaxWidth(),
        )
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
private fun PropertyDetailsLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding(),
        contentAlignment = Alignment.Center,
    ) {
        AppLoadingIndicator(label = "Loading property details")
    }
}

@Composable
private fun PropertyDetailsErrorState(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PropertyDetailsStateScaffold(modifier = modifier) {
        AppErrorState(
            title = "Property details unavailable",
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
private fun PropertyDetailsEmptyState(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PropertyDetailsStateScaffold(modifier = modifier) {
        AppEmptyState(
            title = "No property selected",
            message = "Choose a property opportunity to review its investment details.",
            action = {
                AppOutlinedButton(
                    text = "Back",
                    onClick = onBackClick,
                )
            },
        )
    }
}

@Composable
private fun PropertyDetailsStateScaffold(
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

private fun Int?.asScoreOrDash(): String = this?.toString() ?: "-"

private fun Double?.asPercentOrDash(): String = this?.let { "${it.toOneDecimal()}%" } ?: "-"

private fun Double?.asCompactMoney(currency: String): String {
    val value = this ?: return "-"
    val amount = when {
        value >= 1_000_000.0 -> "${(value / 1_000_000.0).toOneDecimal()}M"
        value >= 1_000.0 -> "${round(value / 1_000.0).toInt()}K"
        else -> round(value).toInt().toString()
    }
    return "$currency $amount"
}

private fun Double?.asMoneyOrDash(currency: String): String {
    val value = this ?: return "-"
    return "$currency ${round(value).toInt()}"
}

private fun Double?.asSignedMoneyOrDash(currency: String): String {
    val value = this ?: return "-"
    val sign = when {
        value > 0.0 -> "+"
        value < 0.0 -> "-"
        else -> ""
    }
    return "$sign$currency ${round(abs(value)).toInt()}"
}

private fun Double?.toMetricAccent(): AppMetricAccent {
    val value = this ?: return AppMetricAccent.Neutral
    return if (value < 0.0) AppMetricAccent.Negative else AppMetricAccent.Positive
}

private fun Double.toOneDecimal(): String {
    val rounded = round(this * 10.0) / 10.0
    return if (rounded == round(rounded)) {
        round(rounded).toInt().toString()
    } else {
        rounded.toString()
    }
}
