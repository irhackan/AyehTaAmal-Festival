package ir.ayeh.taamal.presentation.ayah

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.AyahDetailContent
import ir.ayeh.taamal.domain.model.UserSettings
import ir.ayeh.taamal.ui.components.ArabicAyahText
import ir.ayeh.taamal.ui.components.EmptyState
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AyahDetailViewModel @Inject constructor(
    private val repo: ContentRepository,
    private val prefs: UserPreferencesRepository
) : ViewModel() {
    val settings = prefs.settings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())
    var detail by mutableStateOf<AyahDetailContent?>(null)
        private set

    fun load(ayahId: Long, translatorId: Long) {
        viewModelScope.launch {
            detail = repo.getAyahDetail(ayahId, translatorId)
            repo.bumpProgress("ayahs_read")
            prefs.addPoints(5)
        }
    }

    fun setTranslator(id: Long) {
        viewModelScope.launch { prefs.update { it.copy(translatorId = id) } }
    }

    fun completePractice() {
        viewModelScope.launch {
            repo.bumpProgress("practices_done")
            prefs.addPoints(10)
        }
    }
}

@Composable
fun AyahDetailScreen(
    ayahId: Long,
    viewModel: AyahDetailViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    LaunchedEffect(ayahId, settings.translatorId) {
        viewModel.load(ayahId, settings.translatorId)
    }
    val detail = viewModel.detail

    if (detail == null) {
        EmptyState("آیه یافت نشد یا هنوز بارگذاری نشده است.")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SoftCard {
                Text(
                    "سوره ${detail.ayah.surahNumber} · آیه ${detail.ayah.ayahNumber} · جزء ${detail.ayah.juzNumber}",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))
                ArabicAyahText(detail.ayah.arabicText)
            }
        }
        item {
            SoftCard {
                SectionTitle("ترجمه")
                listOf(1L to "فولادوند", 2L to "مکارم", 3L to "قمشه‌ای").forEach { (id, name) ->
                    FilterChip(
                        selected = settings.translatorId == id,
                        onClick = { viewModel.setTranslator(id) },
                        label = { Text(name) },
                        modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                val selected = detail.translations.find { it.translatorId == settings.translatorId }
                    ?: detail.translations.firstOrNull()
                Text(selected?.translationText.orEmpty(), style = MaterialTheme.typography.bodyLarge)
            }
        }
        if (detail.message.isNotBlank()) {
            item {
                SoftCard {
                    SectionTitle("پیام اصلی")
                    Text(detail.message)
                }
            }
        }
        if (detail.tafsir.isNotBlank()) {
            item {
                SoftCard {
                    SectionTitle("تفسیر کوتاه", detail.tafsirSource)
                    Text(detail.tafsir)
                }
            }
        }
        if (detail.vocabulary.isNotEmpty()) {
            item {
                SoftCard {
                    SectionTitle("واژگان مهم")
                    detail.vocabulary.forEach { v ->
                        Text("${v.arabic} (${v.root})", fontWeight = FontWeight.SemiBold)
                        Text("${v.meaning} — ${v.note}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        }
        if (detail.applications.isNotEmpty()) {
            item {
                SoftCard {
                    SectionTitle("کاربرد امروزی")
                    detail.applications.forEach { (k, v) ->
                        Text(k, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                        Text(v)
                        Spacer(Modifier.height(6.dp))
                    }
                }
            }
        }
        detail.practices.forEach { practice ->
            item {
                SoftCard {
                    SectionTitle(practice.title, "${practice.durationMinutes} دقیقه · ${practice.difficulty}")
                    Text(practice.description)
                    Spacer(Modifier.height(8.dp))
                    PrimaryActionButton("ثبت انجام تمرین") { viewModel.completePractice() }
                }
            }
        }
        item {
            SoftCard {
                SectionTitle("پرسش‌های تدبری")
                listOf(
                    "پیام اصلی این آیه چیست؟",
                    "این آیه با کدام مسئله زندگی من ارتباط دارد؟",
                    "چه رفتاری باید اصلاح شود؟",
                    "امروز چه اقدامی انجام می‌دهم؟"
                ).forEach { Text("• $it", modifier = Modifier) }
            }
        }
    }
}
