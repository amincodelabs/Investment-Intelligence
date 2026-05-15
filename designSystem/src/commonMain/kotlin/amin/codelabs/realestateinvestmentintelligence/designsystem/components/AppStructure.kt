package amin.codelabs.realestateinvestmentintelligence.designsystem.components

import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AppSectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    action: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs)) {
            Text(
                text = title,
                style = MaterialTheme.appTypography.sectionTitle,
                color = MaterialTheme.appColors.onBackground,
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.appTypography.bodySmall,
                    color = MaterialTheme.appColors.mutedText,
                )
            }
        }
        action?.invoke()
    }
}

@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    navigation: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.appColors.background)
            .padding(horizontal = MaterialTheme.appSpacing.screen),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
    ) {
        navigation?.invoke()
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.appTypography.screenTitle,
            color = MaterialTheme.appColors.onBackground,
        )
        actions?.invoke()
    }
}

@Composable
fun AppDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = MaterialTheme.appColors.divider,
    )
}

@Composable
fun AppLoadingIndicator(
    modifier: Modifier = Modifier,
    label: String? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            strokeWidth = 2.5.dp,
            color = MaterialTheme.appColors.primary,
        )
        label?.let {
            Text(
                text = it,
                style = MaterialTheme.appTypography.bodySmall,
                color = MaterialTheme.appColors.mutedText,
            )
        }
    }
}

@Composable
fun AppEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    AppStateContainer(
        title = title,
        message = message,
        modifier = modifier,
        accent = MaterialTheme.appColors.neutralMetric,
        action = action,
    )
}

@Composable
fun AppErrorState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    AppStateContainer(
        title = title,
        message = message,
        modifier = modifier,
        accent = MaterialTheme.appColors.error,
        action = action,
    )
}

@Composable
private fun AppStateContainer(
    title: String,
    message: String,
    accent: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.appSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.md),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(accent.copy(alpha = 0.12f), shape = MaterialTheme.shapes.small),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(accent, shape = MaterialTheme.shapes.small),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.appTypography.sectionTitle,
            color = MaterialTheme.appColors.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            style = MaterialTheme.appTypography.bodySmall,
            color = MaterialTheme.appColors.mutedText,
            textAlign = TextAlign.Center,
        )
        action?.invoke()
    }
}
