package com.agon.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ============================================================
// ZERO-TRUST VAULT — DARK-FIRST CYBERSECURITY THEME
// The app is always rendered in dark mode regardless of system
// setting, to maintain the premium security-product aesthetic.
// ============================================================

private val VaultDarkScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = Primary,

    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = Color(0xFF003E36),
    onSecondaryContainer = Secondary,

    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = Color(0xFF002C45),
    onTertiaryContainer = Tertiary,

    background = BgBase,
    onBackground = TextHi,

    surface = BgSurface,
    onSurface = TextHi,
    surfaceVariant = BgSurfaceHigh,
    onSurfaceVariant = TextMid,
    surfaceTint = Primary,

    surfaceContainer = BgSurface,
    surfaceContainerLow = BgBase,
    surfaceContainerLowest = BgBase,
    surfaceContainerHigh = BgSurfaceHigh,
    surfaceContainerHighest = BgSurfaceHigher,

    inverseSurface = TextHi,
    inverseOnSurface = BgSurface,

    error = StatusDanger,
    onError = Color(0xFF2A0710),
    errorContainer = StatusDangerDim,
    onErrorContainer = Color(0xFFFFD6DE),

    outline = BgOutline,
    outlineVariant = BgOutlineHigh,

    scrim = Color(0xCC000000),
)

@Composable
fun AgonAppTheme(
    darkTheme: Boolean = true, // dark-first, ignore system
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = VaultDarkScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = BgBase.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}