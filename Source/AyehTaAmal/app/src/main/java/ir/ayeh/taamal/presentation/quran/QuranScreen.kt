package ir.ayeh.taamal.presentation.quran

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.Ayah
import ir.ayeh.taamal.ui.components.ArabicAyahText
import ir.ayeh.taamal.ui.components.EmptyState
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class QuranViewModel @Inject constructor(repo: ContentRepository) : ViewModel() {
    val surahs = repo.observeSurahs().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@HiltViewModel
class SurahViewModel @Inject constructor(private val repo: ContentRepository) : ViewModel() {
    var ayahs by mutableStateOf<List<Ayah>>(emptyList())
        private set

    fun load(surah: Int) {
        viewModelScope.launch { ayahs = repo.getAyahsBySurah(surah) }
    }
}

@Composable
fun QuranScreen(
    onOpenSurah: (Int) -> Unit,
    viewModel: QuranViewModel = hiltViewModel()
) {
    val surahs by viewModel.surahs.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SectionTitle("قرآن کریم", "فهرست سوره‌ها") }
        items(surahs, key = { it.number }) { s ->
            SoftCard(modifier = Modifier.clickable { onOpenSurah(s.number) }) {
                Row(Modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("${s.number}. ${s.nameFa}", style = MaterialTheme.typography.titleLarge)
                        Text(s.nameAr, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Column {
                        Text("${s.ayahCount} آیه", fontWeight = FontWeight.Medium)
                        Text("${s.revelationType} · جزء ${s.juzStart}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun SurahScreen(
    surahNumber: Int,
    onOpenAyah: (Long) -> Unit,
    viewModel: SurahViewModel = hiltViewModel()
) {
    LaunchedEffect(surahNumber) { viewModel.load(surahNumber) }
    val ayahs = viewModel.ayahs

    if (ayahs.isEmpty()) {
        EmptyState(
            "در این نسخه، آیات منتخب این سوره هنوز بارگذاری نشده‌اند. از خانه یا جست‌وجو آیات دارای محتوای تدبری را ببینید."
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { SectionTitle("سوره شماره $surahNumber") }
            items(ayahs, key = { it.id }) { ayah ->
                SoftCard(modifier = Modifier.clickable { onOpenAyah(ayah.id) }) {
                    Text("آیه ${ayah.ayahNumber}", color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(6.dp))
                    ArabicAyahText(ayah.arabicText)
                    Spacer(Modifier.height(4.dp))
                    Text(ayah.topic, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
