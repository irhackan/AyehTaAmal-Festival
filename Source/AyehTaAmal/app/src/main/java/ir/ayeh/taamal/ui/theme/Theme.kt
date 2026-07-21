package ir.ayeh.taamal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

enum class AppColorScheme(val key: String, val titleFa: String) {
    QURANIC_GREEN("quranic_green", "سبز قرآنی"),
    TURQUOISE("turquoise", "فیروزه‌ای"),
    CALM_BLUE("calm_blue", "آبی آرامش"),
    GOLD_CREAM("gold_cream", "طلایی و کرم"),
    SOFT_PURPLE("soft_purple", "بنفش ملایم"),
    NIGHT_NAVY("night_navy", "شب سرمه‌ای"),
    NIGHT_BLACK("night_black", "شب مشکی");

    companion object {
        fun fromKey(key: String): AppColorScheme =
            entries.find { it.key == key } ?: QURANIC_GREEN
    }
}

data class AyehExtraColors(
    val cream: Color,
    val gold: Color,
    val softSurface: Color,
    val ayahHighlight: Color,
    val success: Color,
    val cardShadow: Color
)

val LocalAyehColors = staticCompositionLocalOf {
    AyehExtraColors(
        cream = Color(0xFFF5E6C8),
        gold = Color(0xFFC9A227),
        softSurface = Color(0xFFF7F3EA),
        ayahHighlight = Color(0xFF1F6B4F),
        success = Color(0xFF2E8B57),
        cardShadow = Color(0x33000000)
    )
}

val LocalFontScales = staticCompositionLocalOf { 1f to 1f }

private fun palette(scheme: AppColorScheme, dark: Boolean): Pair<androidx.compose.material3.ColorScheme, AyehExtraColors> {
    return when (scheme) {
        AppColorScheme.QURANIC_GREEN -> if (dark) {
            darkColorScheme(
                primary = Color(0xFF6FCF97),
                onPrimary = Color(0xFF00391F),
                primaryContainer = Color(0xFF1F6B4F),
                onPrimaryContainer = Color(0xFFD8F5E5),
                secondary = Color(0xFFE0C35A),
                onSecondary = Color(0xFF3A2F00),
                background = Color(0xFF0B1C16),
                onBackground = Color(0xFFE6EFE9),
                surface = Color(0xFF12241C),
                onSurface = Color(0xFFE6EFE9),
                surfaceVariant = Color(0xFF1C3328),
                onSurfaceVariant = Color(0xFFB7C9BE)
            ) to AyehExtraColors(
                cream = Color(0xFF2A2418),
                gold = Color(0xFFE0C35A),
                softSurface = Color(0xFF162820),
                ayahHighlight = Color(0xFF6FCF97),
                success = Color(0xFF81C784),
                cardShadow = Color(0x66000000)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF1F6B4F),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFD4EFE3),
                onPrimaryContainer = Color(0xFF0F3D2E),
                secondary = Color(0xFFC9A227),
                onSecondary = Color(0xFF2B2100),
                background = Color(0xFFF7F3EA),
                onBackground = Color(0xFF1A241E),
                surface = Color(0xFFFFFBF4),
                onSurface = Color(0xFF1A241E),
                surfaceVariant = Color(0xFFE8F0EA),
                onSurfaceVariant = Color(0xFF44554B)
            ) to AyehExtraColors(
                cream = Color(0xFFF5E6C8),
                gold = Color(0xFFC9A227),
                softSurface = Color(0xFFF0EADF),
                ayahHighlight = Color(0xFF1F6B4F),
                success = Color(0xFF2E8B57),
                cardShadow = Color(0x22000000)
            )
        }

        AppColorScheme.TURQUOISE -> if (dark) {
            darkColorScheme(
                primary = Color(0xFF4ECDC4),
                onPrimary = Color(0xFF003733),
                primaryContainer = Color(0xFF0A4F4A),
                secondary = Color(0xFFE8C547),
                background = Color(0xFF071520),
                onBackground = Color(0xFFE5EEF3),
                surface = Color(0xFF0E2130),
                onSurface = Color(0xFFE5EEF3),
                surfaceVariant = Color(0xFF173246)
            ) to AyehExtraColors(
                cream = Color(0xFF1B2730),
                gold = Color(0xFFE8C547),
                softSurface = Color(0xFF102433),
                ayahHighlight = Color(0xFF4ECDC4),
                success = Color(0xFF66BB6A),
                cardShadow = Color(0x66000000)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF0D7377),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFD4F3F2),
                secondary = Color(0xFFD4A017),
                background = Color(0xFFF4FBFC),
                onBackground = Color(0xFF102A37),
                surface = Color.White,
                onSurface = Color(0xFF102A37),
                surfaceVariant = Color(0xFFE2F3F4)
            ) to AyehExtraColors(
                cream = Color(0xFFEAF6F7),
                gold = Color(0xFFD4A017),
                softSurface = Color(0xFFEFF8F9),
                ayahHighlight = Color(0xFF0D7377),
                success = Color(0xFF2E8B57),
                cardShadow = Color(0x22000000)
            )
        }

        AppColorScheme.CALM_BLUE -> if (dark) {
            darkColorScheme(
                primary = Color(0xFF7EB6D9),
                onPrimary = Color(0xFF00344A),
                secondary = Color(0xFFB0BEC5),
                background = Color(0xFF0C1520),
                onBackground = Color(0xFFE8EEF4),
                surface = Color(0xFF152233),
                onSurface = Color(0xFFE8EEF4),
                surfaceVariant = Color(0xFF1E2F42)
            ) to AyehExtraColors(
                cream = Color(0xFF1A2430),
                gold = Color(0xFF90CAF9),
                softSurface = Color(0xFF121C28),
                ayahHighlight = Color(0xFF7EB6D9),
                success = Color(0xFF81C784),
                cardShadow = Color(0x66000000)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF1B4965),
                onPrimary = Color.White,
                primaryContainer = Color(0xFFD6EAF5),
                secondary = Color(0xFF5FA8D3),
                background = Color(0xFFF5F7FA),
                onBackground = Color(0xFF15202B),
                surface = Color.White,
                onSurface = Color(0xFF15202B),
                surfaceVariant = Color(0xFFE8EEF4)
            ) to AyehExtraColors(
                cream = Color(0xFFEEF3F8),
                gold = Color(0xFF5FA8D3),
                softSurface = Color(0xFFF0F4F8),
                ayahHighlight = Color(0xFF1B4965),
                success = Color(0xFF2E8B57),
                cardShadow = Color(0x22000000)
            )
        }

        AppColorScheme.GOLD_CREAM -> if (dark) {
            darkColorScheme(
                primary = Color(0xFFE0C35A),
                onPrimary = Color(0xFF3A2F00),
                secondary = Color(0xFFD7C4A0),
                background = Color(0xFF1A160F),
                onBackground = Color(0xFFF3EAD8),
                surface = Color(0xFF241E15),
                onSurface = Color(0xFFF3EAD8)
            ) to AyehExtraColors(
                cream = Color(0xFF2B2418),
                gold = Color(0xFFE0C35A),
                softSurface = Color(0xFF211B13),
                ayahHighlight = Color(0xFFE0C35A),
                success = Color(0xFFA5D6A7),
                cardShadow = Color(0x66000000)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF8B6914),
                onPrimary = Color.White,
                secondary = Color(0xFFB0892E),
                background = Color(0xFFFAF4E8),
                onBackground = Color(0xFF2A2418),
                surface = Color(0xFFFFF9EF),
                onSurface = Color(0xFF2A2418)
            ) to AyehExtraColors(
                cream = Color(0xFFF5E6C8),
                gold = Color(0xFFC9A227),
                softSurface = Color(0xFFF3EAD7),
                ayahHighlight = Color(0xFF8B6914),
                success = Color(0xFF2E8B57),
                cardShadow = Color(0x22000000)
            )
        }

        AppColorScheme.SOFT_PURPLE -> if (dark) {
            darkColorScheme(
                primary = Color(0xFFCE93D8),
                onPrimary = Color(0xFF3B1048),
                secondary = Color(0xFFE0C35A),
                background = Color(0xFF160F1C),
                onBackground = Color(0xFFF3EAF6),
                surface = Color(0xFF22172A),
                onSurface = Color(0xFFF3EAF6)
            ) to AyehExtraColors(
                cream = Color(0xFF261A30),
                gold = Color(0xFFE0C35A),
                softSurface = Color(0xFF1C1324),
                ayahHighlight = Color(0xFFCE93D8),
                success = Color(0xFF81C784),
                cardShadow = Color(0x66000000)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF6A4C93),
                onPrimary = Color.White,
                secondary = Color(0xFFC9A227),
                background = Color(0xFFF8F4FB),
                onBackground = Color(0xFF24182E),
                surface = Color.White,
                onSurface = Color(0xFF24182E)
            ) to AyehExtraColors(
                cream = Color(0xFFF1E8F7),
                gold = Color(0xFFC9A227),
                softSurface = Color(0xFFF3ECF8),
                ayahHighlight = Color(0xFF6A4C93),
                success = Color(0xFF2E8B57),
                cardShadow = Color(0x22000000)
            )
        }

        AppColorScheme.NIGHT_NAVY -> darkColorScheme(
            primary = Color(0xFFE0C35A),
            onPrimary = Color(0xFF2B2100),
            secondary = Color(0xFF6FCF97),
            background = Color(0xFF0A1220),
            onBackground = Color(0xFFE8EDF4),
            surface = Color(0xFF121C2E),
            onSurface = Color(0xFFE8EDF4),
            surfaceVariant = Color(0xFF1A2740)
        ) to AyehExtraColors(
            cream = Color(0xFF172033),
            gold = Color(0xFFE0C35A),
            softSurface = Color(0xFF101A2A),
            ayahHighlight = Color(0xFFE0C35A),
            success = Color(0xFF6FCF97),
            cardShadow = Color(0x88000000)
        )

        AppColorScheme.NIGHT_BLACK -> darkColorScheme(
            primary = Color(0xFF81C784),
            onPrimary = Color(0xFF003910),
            secondary = Color(0xFFE0C35A),
            background = Color(0xFF0A0A0A),
            onBackground = Color(0xFFEAEAEA),
            surface = Color(0xFF161616),
            onSurface = Color(0xFFEAEAEA),
            surfaceVariant = Color(0xFF222222)
        ) to AyehExtraColors(
            cream = Color(0xFF1C1C1C),
            gold = Color(0xFFE0C35A),
            softSurface = Color(0xFF121212),
            ayahHighlight = Color(0xFF81C784),
            success = Color(0xFF81C784),
            cardShadow = Color(0x99000000)
        )
    }
}

@Composable
fun AyehTaAmalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorSchemeKey: AppColorScheme = AppColorScheme.QURANIC_GREEN,
    arabicFontScale: Float = 1f,
    translationFontScale: Float = 1f,
    content: @Composable () -> Unit
) {
    val forcedDark = colorSchemeKey == AppColorScheme.NIGHT_NAVY ||
        colorSchemeKey == AppColorScheme.NIGHT_BLACK
    val dark = forcedDark || darkTheme
    val (colors, extras) = palette(colorSchemeKey, dark)

    val typography = MaterialTheme.typography.copy(
        displayLarge = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 34.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * translationFontScale).sp,
            lineHeight = (26 * translationFontScale).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * translationFontScale).sp,
            lineHeight = (22 * translationFontScale).sp
        ),
        labelLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    )

    CompositionLocalProvider(
        LocalAyehColors provides extras,
        LocalFontScales provides (arabicFontScale to translationFontScale)
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = typography,
            content = content
        )
    }
}
