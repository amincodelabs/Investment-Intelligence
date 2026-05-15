package amin.codelabs.realestateinvestmentintelligence.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppElevation(
    val none: Dp = 0.dp,
    val subtle: Dp = 1.dp,
    val raised: Dp = 4.dp,
)
