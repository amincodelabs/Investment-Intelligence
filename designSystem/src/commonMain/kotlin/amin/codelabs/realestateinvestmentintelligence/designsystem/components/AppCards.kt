package amin.codelabs.realestateinvestmentintelligence.designsystem.components

import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appBorders
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appElevation
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appShapes
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.appShapes.card,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.appColors.surface,
            contentColor = MaterialTheme.appColors.onSurface,
        ),
        border = MaterialTheme.appBorders.subtle,
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.appElevation.none),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.appSpacing.lg)) {
            content()
        }
    }
}

@Composable
fun AppInfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    accent: AppMetricAccent = AppMetricAccent.Neutral,
) {
    val colors = MaterialTheme.appColors
    val accentColor = when (accent) {
        AppMetricAccent.Positive -> colors.positive
        AppMetricAccent.Negative -> colors.negative
        AppMetricAccent.Warning -> colors.warning
        AppMetricAccent.Neutral -> colors.neutralMetric
    }

    Card(
        modifier = modifier,
        shape = MaterialTheme.appShapes.card,
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface,
        ),
        border = BorderStroke(MaterialTheme.appBorders.subtle.width, MaterialTheme.appColors.divider),
        elevation = CardDefaults.cardElevation(defaultElevation = MaterialTheme.appElevation.subtle),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.appSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.xs),
        ) {
            Text(
                text = title,
                style = MaterialTheme.appTypography.metricLabel,
                color = colors.mutedText,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.appTypography.metricValue,
                    color = accentColor,
                )
                subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.appTypography.caption,
                        color = colors.mutedText,
                    )
                }
            }
        }
    }
}

enum class AppMetricAccent {
    Positive,
    Negative,
    Warning,
    Neutral,
}
