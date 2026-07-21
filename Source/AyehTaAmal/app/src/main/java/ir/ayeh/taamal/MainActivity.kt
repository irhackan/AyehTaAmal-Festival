package ir.ayeh.taamal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ir.ayeh.taamal.presentation.root.RootViewModel
import ir.ayeh.taamal.presentation.root.AppRoot
import ir.ayeh.taamal.ui.theme.AppColorScheme
import ir.ayeh.taamal.ui.theme.AyehTaAmalTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                AyehApp()
            }
        }
    }
}

@Composable
private fun AyehApp(viewModel: RootViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val dark = when (settings.nightMode) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }
    val scheme = AppColorScheme.fromKey(settings.colorScheme)

    AyehTaAmalTheme(
        darkTheme = dark,
        colorSchemeKey = scheme,
        arabicFontScale = settings.arabicFontScale,
        translationFontScale = settings.translationFontScale
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppRoot(viewModel = viewModel)
        }
    }
}
