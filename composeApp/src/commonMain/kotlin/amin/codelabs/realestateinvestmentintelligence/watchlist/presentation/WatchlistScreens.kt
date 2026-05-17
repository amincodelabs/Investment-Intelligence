package amin.codelabs.realestateinvestmentintelligence.watchlist.presentation

import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppCard
import amin.codelabs.realestateinvestmentintelligence.designsystem.components.AppChip
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
fun WatchlistScreen(
    state: WatchlistUiState,
    onEvent: (WatchlistUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        WatchlistUiState.Loading -> WatchlistLoadingState(modifier = modifier)
        WatchlistUiState.Empty -> WatchlistEmptyState(
            modifier = modifier,
            onRetryClick = { onEvent(WatchlistUiEvent.RetryClicked) },
        )

        is WatchlistUiState.Error -> WatchlistErrorState(
            message = state.message,
            modifier = modifier,
            onRetryClick = { onEvent(WatchlistUiEvent.RetryClicked) },
        )

        is WatchlistUiState.Content -> WatchlistContentState(
            content = state.content,
            modifier = modifier,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun WatchlistContentState(
    content: WatchlistContent,
    onEvent: (WatchlistUiEvent) -> Unit,
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
            WatchlistHeader(
                savedProperties = content.savedProperties.size,
                savedAreas = content.savedAreas.size,
            )
        }
        item {
            SavedPropertiesSection(
                properties = content.savedProperties,
                onEvent = onEvent,
            )
        }
        item {
            SavedAreasSection(
                areas = content.savedAreas,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun WatchlistHeader(
    savedProperties: Int,
    savedAreas: Int,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
        Text(
            text = "Watchlist",
            style = MaterialTheme.appTypography.screenTitle,
            color = MaterialTheme.appColors.onBackground,
        )
        Text(
            text = "Saved investment opportunities for later review.",
            style = MaterialTheme.appTypography.bodySmall,
            color = MaterialTheme.appColors.mutedText,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
        ) {
            AppInfoCard(
                title = "Properties",
                value = savedProperties.toString(),
                subtitle = "saved",
                accent = AppMetricAccent.Neutral,
                modifier = Modifier.weight(1f),
            )
            AppInfoCard(
                title = "Areas",
                value = savedAreas.toString(),
                subtitle = "saved",
                accent = AppMetricAccent.Neutral,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SavedPropertiesSection(
    properties: List<WatchlistPropertyItemUi>,
    onEvent: (WatchlistUiEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Saved properties",
            subtitle = "Properties on your shortlist",
        )
        if (properties.isEmpty()) {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "No saved properties yet.",
                    style = MaterialTheme.appTypography.bodySmall,
                    color = MaterialTheme.appColors.mutedText,
                )
            }
        } else {
            properties.forEach { property ->
                WatchlistPropertyCard(
                    property = property,
                    onOpen = { onEvent(WatchlistUiEvent.PropertyClicked(property.id)) },
                    onRemove = { onEvent(WatchlistUiEvent.RemovePropertyClicked(property.id)) },
                )
            }
        }
    }
}

@Composable
private fun SavedAreasSection(
    areas: List<WatchlistAreaItemUi>,
    onEvent: (WatchlistUiEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md)) {
        AppSectionTitle(
            title = "Saved areas",
            subtitle = "Areas worth monitoring",
        )
        if (areas.isEmpty()) {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "No saved areas yet.",
                    style = MaterialTheme.appTypography.bodySmall,
                    color = MaterialTheme.appColors.mutedText,
                )
            }
        } else {
            areas.forEach { area ->
                WatchlistAreaCard(
                    area = area,
                    onOpen = { onEvent(WatchlistUiEvent.AreaClicked(area.id)) },
                    onRemove = { onEvent(WatchlistUiEvent.RemoveAreaClicked(area.id)) },
                )
            }
        }
    }
}

@Composable
private fun WatchlistPropertyCard(
    property: WatchlistPropertyItemUi,
    onOpen: () -> Unit,
    onRemove: () -> Unit,
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
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
                        text = "Area: ${property.areaId}",
                        style = MaterialTheme.appTypography.bodySmall,
                        color = MaterialTheme.appColors.mutedText,
                    )
                }
                AppTertiaryButton(
                    text = "Remove",
                    onClick = onRemove,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                AppChip(text = property.priceAmount.asMoney(property.currency))
                property.netRentalYieldPercentage?.let { AppChip(text = "Yield ${it.asPercent()}") }
                property.investmentScore?.let { AppChip(text = "Score $it") }
            }
        }
    }
}

@Composable
private fun WatchlistAreaCard(
    area: WatchlistAreaItemUi,
    onOpen: () -> Unit,
    onRemove: () -> Unit,
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen),
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
                        text = "Saved area",
                        style = MaterialTheme.appTypography.bodySmall,
                        color = MaterialTheme.appColors.mutedText,
                    )
                }
                AppTertiaryButton(
                    text = "Remove",
                    onClick = onRemove,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm)) {
                area.averagePriceAmount?.let { AppChip(text = it.asMoney(area.currency)) }
                area.averageRentalYieldPercentage?.let { AppChip(text = "Yield ${it.asPercent()}") }
                area.demandLabel?.let { AppChip(text = "Demand $it") }
                area.riskLabel?.let { AppChip(text = "Risk $it") }
            }
        }
    }
}

@Composable
private fun WatchlistLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.appColors.background)
            .safeContentPadding(),
        contentAlignment = Alignment.Center,
    ) {
        AppLoadingIndicator(label = "Loading watchlist")
    }
}

@Composable
private fun WatchlistEmptyState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WatchlistStateScaffold(modifier = modifier) {
        AppEmptyState(
            title = "Watchlist is empty",
            message = "Saved properties and areas will appear here when you add them.",
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
private fun WatchlistErrorState(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WatchlistStateScaffold(modifier = modifier) {
        AppErrorState(
            title = "Watchlist unavailable",
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
private fun WatchlistStateScaffold(
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

private fun Double.asMoney(currency: String): String = "$currency ${round(this).toInt()}"

private fun Double.asPercent(): String {
    val rounded = round(this * 10.0) / 10.0
    return if (rounded == round(rounded)) {
        "${round(rounded).toInt()}%"
    } else {
        "$rounded%"
    }
}
