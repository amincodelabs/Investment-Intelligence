package amin.codelabs.realestateinvestmentintelligence.designsystem.previews

import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppCheckbox
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppChip
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppDivider
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppEmptyState
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppErrorState
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppInfoCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppLoadingButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppLoadingIndicator
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppMetricAccent
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppOutlinedButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppPasswordTextField
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppPrimaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppSecondaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppSectionTitle
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTertiaryButton
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTextField
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppTopBar
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.AppTheme
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun AppButtonsLightPreview() {
    AppTheme(darkTheme = false) {
        PreviewSurface {
            AppPrimaryButton(text = "Continue", onClick = {})
            AppPrimaryButton(text = "Disabled", onClick = {}, enabled = false)
            AppSecondaryButton(text = "Review", onClick = {})
            AppOutlinedButton(text = "Compare", onClick = {})
            AppTertiaryButton(text = "View details", onClick = {})
            AppLoadingButton(text = "Calculating", onClick = {}, loading = true)
        }
    }
}

@Preview
@Composable
private fun AppButtonsDarkPreview() {
    AppTheme(darkTheme = true) {
        PreviewSurface {
            AppPrimaryButton(text = "Continue", onClick = {})
            AppSecondaryButton(text = "Review", onClick = {})
            AppOutlinedButton(text = "Compare", onClick = {})
            AppLoadingButton(text = "Calculating", onClick = {}, loading = true)
        }
    }
}

@Preview
@Composable
private fun AppInputsPreview() {
    AppTheme {
        PreviewSurface {
            AppTextField(
                value = "1,250,000",
                onValueChange = {},
                label = "Amount",
            )
            AppTextField(
                value = "",
                onValueChange = {},
                label = "Estimate",
                placeholder = "Annual value",
                supportingText = "Enter a valid numeric estimate.",
                isError = true,
            )
            AppPasswordTextField(
                value = "password",
                onValueChange = {},
                label = "Password",
            )
            AppCheckbox(
                checked = true,
                onCheckedChange = {},
                label = "Include saved opportunities",
            )
        }
    }
}

@Preview
@Composable
private fun AppCardsAndChipsPreview() {
    AppTheme {
        PreviewSurface {
            AppSectionTitle(
                title = "Investment summary",
                subtitle = "Estimated metrics for the selected opportunity",
            )
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                AppChip(text = "Yield", selected = true)
                AppChip(text = "Risk")
                AppChip(text = "Disabled", enabled = false)
            }
            AppCard {
                Text(
                    text = "A quiet card container for grouped dashboard content.",
                    style = MaterialTheme.appTypography.body,
                    color = MaterialTheme.appColors.onSurface,
                )
            }
            AppInfoCard(
                title = "Net return",
                value = "6.8%",
                subtitle = "estimated",
                accent = AppMetricAccent.Positive,
            )
            AppInfoCard(
                title = "Variance",
                value = "-2.1%",
                subtitle = "period",
                accent = AppMetricAccent.Negative,
            )
        }
    }
}

@Preview
@Composable
private fun AppStructureStatesPreview() {
    AppTheme {
        PreviewSurface {
            AppTopBar(title = "Portfolio")
            AppDivider()
            AppLoadingIndicator(label = "Loading market metrics")
            AppEmptyState(
                title = "No saved opportunities",
                message = "Saved areas and properties will appear here.",
                action = { AppOutlinedButton(text = "Explore areas", onClick = {}) },
            )
            AppErrorState(
                title = "Unable to load data",
                message = "Check your connection and try again.",
                action = { AppPrimaryButton(text = "Retry", onClick = {}) },
            )
        }
    }
}

@Composable
private fun PreviewSurface(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.appColors.background)
            .padding(MaterialTheme.appSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
    ) {
        content()
    }
}
