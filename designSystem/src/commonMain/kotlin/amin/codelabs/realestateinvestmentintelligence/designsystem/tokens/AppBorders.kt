package amin.codelabs.realestateinvestmentintelligence.designsystem.tokens

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class AppBorders(
    val default: BorderStroke,
    val focused: BorderStroke,
    val error: BorderStroke,
    val subtle: BorderStroke,
)

internal fun defaultAppBorders(colors: AppColors) = AppBorders(
    default = BorderStroke(1.dp, colors.outline),
    focused = BorderStroke(1.5.dp, colors.primary),
    error = BorderStroke(1.dp, colors.error),
    subtle = BorderStroke(1.dp, colors.divider),
)
