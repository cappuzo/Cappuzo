package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class AppThemeMode(
    val id: String,
    val titleArabic: String,
    val subtitleArabic: String
) {
    CLASSICAL_GOLD(
        id = "classical_gold",
        titleArabic = "الذهب الكلاسيكي (افتراضي)",
        subtitleArabic = "سواد ملكي عتيق مع تطريزات ولمسات ذهبية فاخرة"
    ),
    OLED_BLACK(
        id = "oled_black",
        titleArabic = "أسود أوليد فائق التوفير (OLED Black)",
        subtitleArabic = "سواد نقي 100% لإطفاء بكسلات الشاشة وتوفير البطارية"
    )
}

val LocalAppThemeMode = compositionLocalOf { AppThemeMode.CLASSICAL_GOLD }

private val ClassicalGoldColorScheme = darkColorScheme(
    primary = GoldRoyal,
    onPrimary = BlackObsidian,
    primaryContainer = BlackCardElevated,
    onPrimaryContainer = GoldChampagne,
    secondary = GoldChampagne,
    onSecondary = BlackObsidian,
    secondaryContainer = BlackCardHover,
    onSecondaryContainer = TextIvoryPrimary,
    tertiary = EmeraldArabesque,
    onTertiary = TextIvoryPrimary,
    background = BlackObsidian,
    onBackground = TextIvoryPrimary,
    surface = BlackCard,
    onSurface = TextIvoryPrimary,
    surfaceVariant = BlackCardElevated,
    onSurfaceVariant = TextIvorySecondary,
    outline = GoldBorder,
    outlineVariant = GoldBorderSubtle,
    error = ErrorRed
)

private val OledBlackColorScheme = darkColorScheme(
    primary = GoldRoyal,
    onPrimary = OledPureBlack,
    primaryContainer = OledCardElevated,
    onPrimaryContainer = GoldChampagne,
    secondary = GoldChampagne,
    onSecondary = OledPureBlack,
    secondaryContainer = OledCard,
    onSecondaryContainer = TextIvoryPrimary,
    tertiary = EmeraldArabesque,
    onTertiary = TextIvoryPrimary,
    background = OledPureBlack,
    onBackground = TextIvoryPrimary,
    surface = OledCard,
    onSurface = TextIvoryPrimary,
    surfaceVariant = OledCardElevated,
    onSurfaceVariant = TextIvorySecondary,
    outline = OledBorder,
    outlineVariant = OledBorderSubtle,
    error = ErrorRed
)

@Composable
fun CappuzoTheme(
    themeMode: AppThemeMode = AppThemeMode.CLASSICAL_GOLD,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.CLASSICAL_GOLD -> ClassicalGoldColorScheme
        AppThemeMode.OLED_BLACK -> OledBlackColorScheme
    }

    val statusBarBg = if (themeMode == AppThemeMode.OLED_BLACK) OledPureBlack else BlackObsidian

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = statusBarBg.toArgb()
                window.navigationBarColor = statusBarBg.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    CompositionLocalProvider(LocalAppThemeMode provides themeMode) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}
