package amin.codelabs.realestateinvestmentintelligence.designsystem.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes(
    val small: CornerBasedShape,
    val textField: CornerBasedShape,
    val button: CornerBasedShape,
    val card: CornerBasedShape,
    val largeContainer: CornerBasedShape,
)

internal val DefaultAppShapes = AppShapes(
    small = RoundedCornerShape(6.dp),
    textField = RoundedCornerShape(8.dp),
    button = RoundedCornerShape(8.dp),
    card = RoundedCornerShape(8.dp),
    largeContainer = RoundedCornerShape(12.dp),
)
