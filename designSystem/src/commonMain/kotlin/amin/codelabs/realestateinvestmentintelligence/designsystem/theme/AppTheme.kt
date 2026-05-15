package amin.codelabs.realestateinvestmentintelligence.designsystem.theme

import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.AppBorders
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.AppColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.AppElevation
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.AppShapes
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.AppSpacing
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.AppTypography
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.DarkAppColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.DefaultAppShapes
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.DefaultAppTypography
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.LightAppColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.tokens.defaultAppBorders
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalAppColors = staticCompositionLocalOf { LightAppColors }
private val LocalAppTypography = staticCompositionLocalOf { DefaultAppTypography }
private val LocalAppShapes = staticCompositionLocalOf { DefaultAppShapes }
private val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }
private val LocalAppBorders = staticCompositionLocalOf { defaultAppBorders(LightAppColors) }
private val LocalAppElevation = staticCompositionLocalOf { AppElevation() }

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkAppColors else LightAppColors
    val typography = DefaultAppTypography
    val shapes = DefaultAppShapes

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes,
        LocalAppSpacing provides AppSpacing(),
        LocalAppBorders provides defaultAppBorders(colors),
        LocalAppElevation provides AppElevation(),
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(darkTheme),
            typography = typography.toMaterialTypography(),
            shapes = shapes.toMaterialShapes(),
            content = content,
        )
    }
}

val MaterialTheme.appColors: AppColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAppColors.current

val MaterialTheme.appTypography: AppTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalAppTypography.current

val MaterialTheme.appShapes: AppShapes
    @Composable
    @ReadOnlyComposable
    get() = LocalAppShapes.current

val MaterialTheme.appSpacing: AppSpacing
    @Composable
    @ReadOnlyComposable
    get() = LocalAppSpacing.current

val MaterialTheme.appBorders: AppBorders
    @Composable
    @ReadOnlyComposable
    get() = LocalAppBorders.current

val MaterialTheme.appElevation: AppElevation
    @Composable
    @ReadOnlyComposable
    get() = LocalAppElevation.current

private fun AppColors.toMaterialColorScheme(darkTheme: Boolean): ColorScheme {
    return if (darkTheme) {
        darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            outline = outline,
            error = error,
            onError = onError,
        )
    } else {
        lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            secondary = secondary,
            onSecondary = onSecondary,
            background = background,
            onBackground = onBackground,
            surface = surface,
            onSurface = onSurface,
            surfaceVariant = surfaceVariant,
            outline = outline,
            error = error,
            onError = onError,
        )
    }
}

private fun AppTypography.toMaterialTypography() = Typography(
    displayLarge = display,
    headlineMedium = screenTitle,
    titleMedium = sectionTitle,
    bodyLarge = body,
    bodyMedium = bodySmall,
    labelLarge = button,
    labelMedium = label,
    labelSmall = caption,
)

private fun AppShapes.toMaterialShapes() = Shapes(
    small = small,
    medium = card,
    large = largeContainer,
)
