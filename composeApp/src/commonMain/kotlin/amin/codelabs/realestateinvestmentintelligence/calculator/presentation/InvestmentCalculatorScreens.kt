package amin.codelabs.realestateinvestmentintelligence.calculator.presentation

import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppDivider
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppErrorState
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppInfoCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppLoadingButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppMetricAccent
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppOutlinedButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppSectionTitle
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTextField
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import kotlin.math.abs
import kotlin.math.round

@Composable
fun InvestmentCalculatorScreen(
    state: InvestmentCalculatorUiState,
    onEvent: (InvestmentCalculatorUiEvent) -> Unit,
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
            CalculatorHeader()
        }
        item {
            CalculatorInputSection(
                state = state,
                onEvent = onEvent,
            )
        }
        if (state.isError && state.errorMessage != null) {
            item {
                AppErrorState(
                    title = "Calculation unavailable",
                    message = state.errorMessage,
                )
            }
        }
        state.result?.let { result ->
            item {
                CalculatorResultsSection(result = result)
            }
        }
        item {
            Text(
                text = "Calculator results are estimates based on entered assumptions and are not financial advice.",
                style = MaterialTheme.appTypography.caption,
                color = MaterialTheme.appColors.mutedText,
            )
        }
    }
}

@Composable
private fun CalculatorHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
        Text(
            text = "Investment calculator",
            style = MaterialTheme.appTypography.screenTitle,
            color = MaterialTheme.appColors.onBackground,
        )
        Text(
            text = "Model yield, cash flow, and estimated ROI from property assumptions.",
            style = MaterialTheme.appTypography.bodySmall,
            color = MaterialTheme.appColors.mutedText,
        )
    }
}

@Composable
private fun CalculatorInputSection(
    state: InvestmentCalculatorUiState,
    onEvent: (InvestmentCalculatorUiEvent) -> Unit,
) {
    val form = state.form
    val errors = state.validationErrors
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
            AppSectionTitle(
                title = "Inputs",
                subtitle = "AED values unless noted",
            )
            CalculatorTextField(
                value = form.propertyPrice,
                label = "Property price",
                placeholder = "1200000",
                error = errors.propertyPrice,
                onValueChange = { onEvent(InvestmentCalculatorUiEvent.PropertyPriceChanged(it)) },
            )
            CalculatorTextField(
                value = form.annualRent,
                label = "Expected annual rent",
                placeholder = "90000",
                error = errors.annualRent,
                onValueChange = { onEvent(InvestmentCalculatorUiEvent.AnnualRentChanged(it)) },
            )
            MetricGrid(
                first = {
                    CalculatorTextField(
                        value = form.serviceCharges,
                        label = "Service charges",
                        placeholder = "12000",
                        error = errors.serviceCharges,
                        onValueChange = { onEvent(InvestmentCalculatorUiEvent.ServiceChargesChanged(it)) },
                    )
                },
                second = {
                    CalculatorTextField(
                        value = form.otherCosts,
                        label = "Other annual costs",
                        placeholder = "6000",
                        error = errors.otherCosts,
                        onValueChange = { onEvent(InvestmentCalculatorUiEvent.OtherCostsChanged(it)) },
                    )
                },
            )
            MetricGrid(
                first = {
                    CalculatorTextField(
                        value = form.sizeSqft,
                        label = "Size sqft",
                        placeholder = "850",
                        error = errors.sizeSqft,
                        onValueChange = { onEvent(InvestmentCalculatorUiEvent.SizeSqftChanged(it)) },
                    )
                },
                second = {
                    CalculatorTextField(
                        value = form.occupancyRate,
                        label = "Occupancy %",
                        placeholder = "100",
                        error = errors.occupancyRate,
                        onValueChange = { onEvent(InvestmentCalculatorUiEvent.OccupancyRateChanged(it)) },
                    )
                },
            )
            CalculatorTextField(
                value = form.appreciationRate,
                label = "Appreciation % optional",
                placeholder = "4",
                error = errors.appreciationRate,
                onValueChange = { onEvent(InvestmentCalculatorUiEvent.AppreciationRateChanged(it)) },
            )
            AppDivider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
            ) {
                AppOutlinedButton(
                    text = "Reset",
                    onClick = { onEvent(InvestmentCalculatorUiEvent.ResetClicked) },
                    modifier = Modifier.weight(1f),
                )
                AppLoadingButton(
                    text = "Calculate",
                    onClick = { onEvent(InvestmentCalculatorUiEvent.CalculateClicked) },
                    loading = state.isLoading,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun CalculatorResultsSection(result: InvestmentCalculatorResultUi) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Results",
            subtitle = "Estimated investment metrics",
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Gross yield",
                    value = result.grossRentalYieldPercentage.asPercent(),
                    subtitle = "annual",
                    accent = AppMetricAccent.Positive,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Net yield",
                    value = result.netRentalYieldPercentage.asPercent(),
                    subtitle = "after costs",
                    accent = result.netRentalYieldPercentage.toMetricAccent(),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Monthly rent",
                    value = result.monthlyRentAmount.asMoney(result.currency),
                    subtitle = "adjusted",
                    accent = AppMetricAccent.Positive,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Monthly costs",
                    value = result.monthlyCostsAmount.asMoney(result.currency),
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
                    value = result.monthlyCashFlowAmount.asSignedMoney(result.currency),
                    subtitle = "net",
                    accent = result.monthlyCashFlowAmount.toMetricAccent(),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Annual cash flow",
                    value = result.annualCashFlowAmount.asSignedMoney(result.currency),
                    subtitle = "net",
                    accent = result.annualCashFlowAmount.toMetricAccent(),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
        MetricGrid(
            first = {
                AppInfoCard(
                    title = "Price per sqft",
                    value = result.pricePerSqftAmount.asMoney(result.currency),
                    subtitle = "sqft",
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            second = {
                AppInfoCard(
                    title = "Simple ROI",
                    value = result.simpleRoiPercentage?.asPercent() ?: "-",
                    subtitle = "with appreciation",
                    accent = result.simpleRoiPercentage.toMetricAccent(),
                    modifier = Modifier.fillMaxWidth(),
                )
            },
        )
    }
}

@Composable
private fun CalculatorTextField(
    value: String,
    label: String,
    placeholder: String,
    error: String?,
    onValueChange: (String) -> Unit,
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        supportingText = error,
        isError = error != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
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

private fun Double.asPercent(): String = "${toOneDecimal()}%"

private fun Double.asMoney(currency: String): String {
    return "$currency ${round(this).toInt()}"
}

private fun Double.asSignedMoney(currency: String): String {
    val sign = when {
        this > 0.0 -> "+"
        this < 0.0 -> "-"
        else -> ""
    }
    return "$sign$currency ${round(abs(this)).toInt()}"
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
