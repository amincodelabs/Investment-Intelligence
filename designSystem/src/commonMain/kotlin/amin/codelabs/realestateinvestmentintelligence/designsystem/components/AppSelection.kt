package amin.codelabs.realestateinvestmentintelligence.designsystem.components

import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appShapes
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm),
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.appColors.primary,
                uncheckedColor = MaterialTheme.appColors.outline,
                checkmarkColor = MaterialTheme.appColors.onPrimary,
                disabledCheckedColor = MaterialTheme.appColors.surfaceVariant,
                disabledUncheckedColor = MaterialTheme.appColors.surfaceVariant,
            ),
        )
        Text(
            text = label,
            style = MaterialTheme.appTypography.bodySmall,
            color = if (enabled) MaterialTheme.appColors.onSurface else MaterialTheme.appColors.mutedText,
        )
    }
}

@Composable
fun AppChip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    val colors = MaterialTheme.appColors
    AssistChip(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        label = {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = MaterialTheme.appSpacing.xs),
                style = MaterialTheme.appTypography.label,
            )
        },
        shape = MaterialTheme.appShapes.small,
        border = BorderStroke(1.dp, if (selected) colors.primary else colors.outline),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) colors.surfaceVariant else colors.surface,
            labelColor = if (selected) colors.primary else colors.onSurface,
            disabledContainerColor = colors.surfaceVariant,
            disabledLabelColor = colors.mutedText,
        ),
    )
}
