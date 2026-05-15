package amin.codelabs.realestateinvestmentintelligence.designsystem.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val outline: Color,
    val error: Color,
    val onError: Color,
    val success: Color,
    val warning: Color,
    val info: Color,
    val positive: Color,
    val negative: Color,
    val neutralMetric: Color,
    val mutedText: Color,
    val divider: Color,
)

internal val LightAppColors = AppColors(
    primary = Color(0xFF143D35),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFC6A15B),
    onSecondary = Color(0xFF19140A),
    background = Color(0xFFF7F8F5),
    onBackground = Color(0xFF111816),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF18201D),
    surfaceVariant = Color(0xFFE9EEE8),
    outline = Color(0xFFCBD4CE),
    error = Color(0xFFB42318),
    onError = Color(0xFFFFFFFF),
    success = Color(0xFF13795B),
    warning = Color(0xFFB7791F),
    info = Color(0xFF2563A8),
    positive = Color(0xFF0E7A54),
    negative = Color(0xFFC2412D),
    neutralMetric = Color(0xFF56625D),
    mutedText = Color(0xFF64716C),
    divider = Color(0xFFE0E6E2),
)

internal val DarkAppColors = AppColors(
    primary = Color(0xFF9DD7C9),
    onPrimary = Color(0xFF052A23),
    secondary = Color(0xFFE2C47E),
    onSecondary = Color(0xFF2D230D),
    background = Color(0xFF0B1110),
    onBackground = Color(0xFFE8EEEA),
    surface = Color(0xFF121A18),
    onSurface = Color(0xFFE5ECE8),
    surfaceVariant = Color(0xFF1D2825),
    outline = Color(0xFF394A45),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    success = Color(0xFF72D7B5),
    warning = Color(0xFFFFD28A),
    info = Color(0xFFA8C7FA),
    positive = Color(0xFF78D9B8),
    negative = Color(0xFFFFB4A5),
    neutralMetric = Color(0xFFA8B5AF),
    mutedText = Color(0xFF9BA8A2),
    divider = Color(0xFF263632),
)
