package amin.codelabs.realestateinvestmentintelligence.designsystem.components

import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appShapes
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
        shape = MaterialTheme.appShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.appColors.primary,
            contentColor = MaterialTheme.appColors.onPrimary,
            disabledContainerColor = MaterialTheme.appColors.surfaceVariant,
            disabledContentColor = MaterialTheme.appColors.mutedText,
        ),
    ) {
        Text(text = text, style = MaterialTheme.appTypography.button)
    }
}

@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
        shape = MaterialTheme.appShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.appColors.secondary,
            contentColor = MaterialTheme.appColors.onSecondary,
            disabledContainerColor = MaterialTheme.appColors.surfaceVariant,
            disabledContentColor = MaterialTheme.appColors.mutedText,
        ),
    ) {
        Text(text = text, style = MaterialTheme.appTypography.button)
    }
}

@Composable
fun AppTertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 44.dp),
        enabled = enabled,
        shape = MaterialTheme.appShapes.button,
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.appColors.primary,
            disabledContentColor = MaterialTheme.appColors.mutedText,
        ),
    ) {
        Text(text = text, style = MaterialTheme.appTypography.button)
    }
}

@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled,
        shape = MaterialTheme.appShapes.button,
        border = BorderStroke(1.dp, MaterialTheme.appColors.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.appColors.primary,
            disabledContentColor = MaterialTheme.appColors.mutedText,
        ),
    ) {
        Text(text = text, style = MaterialTheme.appTypography.button)
    }
}

@Composable
fun AppLoadingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 48.dp),
        enabled = enabled && !loading,
        shape = MaterialTheme.appShapes.button,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.appColors.primary,
            contentColor = MaterialTheme.appColors.onPrimary,
            disabledContainerColor = MaterialTheme.appColors.primary,
            disabledContentColor = MaterialTheme.appColors.onPrimary,
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.appSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.appColors.onPrimary,
                )
            }
            Text(text = text, style = MaterialTheme.appTypography.button)
        }
    }
}
