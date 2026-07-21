package ir.ayeh.taamal.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.domain.model.UserSettings
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import ir.ayeh.taamal.ui.theme.AppColorScheme
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(private val prefs: UserPreferencesRepository) : ViewModel() {
    val settings = prefs.settings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    fun update(transform: (UserSettings) -> UserSettings) {
        viewModelScope.launch { prefs.update(transform) }
    }
}

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle("تنظیمات", "نمایش، قلم، ترجمه و محتوا")

        SoftCard {
            Text("حالت روشن / شب", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            listOf("system" to "خودکار", "light" to "روشن", "dark" to "شب").forEach { (k, l) ->
                FilterChip(
                    selected = settings.nightMode == k,
                    onClick = { viewModel.update { it.copy(nightMode = k) } },
                    label = { Text(l) },
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }

        SoftCard {
            Text("رنگ‌بندی", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            AppColorScheme.entries.forEach { scheme ->
                FilterChip(
                    selected = settings.colorScheme == scheme.key,
                    onClick = { viewModel.update { it.copy(colorScheme = scheme.key) } },
                    label = { Text(scheme.titleFa) },
                    modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                )
            }
        }

        SoftCard {
            Text("اندازه قلم عربی: ${"%.1f".format(settings.arabicFontScale)}")
            Slider(
                value = settings.arabicFontScale,
                onValueChange = { v -> viewModel.update { it.copy(arabicFontScale = v) } },
                valueRange = 0.9f..1.8f
            )
            Text("اندازه ترجمه: ${"%.1f".format(settings.translationFontScale)}")
            Slider(
                value = settings.translationFontScale,
                onValueChange = { v -> viewModel.update { it.copy(translationFontScale = v) } },
                valueRange = 0.9f..1.5f
            )
        }

        SoftCard {
            Text("مترجم پیش‌فرض", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            listOf(1L to "فولادوند", 2L to "مکارم شیرازی", 3L to "الهی قمشه‌ای").forEach { (id, name) ->
                FilterChip(
                    selected = settings.translatorId == id,
                    onClick = { viewModel.update { it.copy(translatorId = id) } },
                    label = { Text(name) },
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }

        SoftCard {
            Text("نوع کاربر", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            listOf(
                "general" to "عمومی",
                "student" to "دانشجو",
                "teen" to "نوجوان",
                "family" to "خانواده"
            ).forEach { (k, l) ->
                FilterChip(
                    selected = settings.audienceType == k,
                    onClick = { viewModel.update { it.copy(audienceType = k) } },
                    label = { Text(l) },
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }

        SoftCard {
            Text("ساعت یادآوری روزانه", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            listOf("06:00", "07:00", "08:00", "12:00", "21:00").forEach { t ->
                FilterChip(
                    selected = settings.notificationTime == t,
                    onClick = { viewModel.update { it.copy(notificationTime = t) } },
                    label = { Text(t) },
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "اعلان‌ها در نسخه بعدی با WorkManager فعال می‌شوند.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
