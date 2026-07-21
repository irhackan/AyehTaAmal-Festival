package ir.ayeh.taamal.presentation.situations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.LifeSituation
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SituationsViewModel @Inject constructor(repo: ContentRepository) : ViewModel() {
    val situations = repo.observeSituations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@Composable
fun SituationsScreen(
    onOpenSituation: (Long) -> Unit,
    viewModel: SituationsViewModel = hiltViewModel()
) {
    val list by viewModel.situations.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { SectionTitle("از موقعیت زندگی تا آیه", "مسئله‌ات را انتخاب کن") }
        items(list, key = { it.id }) { item ->
            SoftCard(modifier = Modifier.clickable { onOpenSituation(item.id) }) {
                Text(item.title, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun SituationDetailScreen(
    situation: LifeSituation?,
    onOpenAyah: (Long) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(situation?.title ?: "موقعیت")
        SoftCard {
            Text("توضیح", style = MaterialTheme.typography.titleMedium)
            Text(situation?.shortExplain.orEmpty())
            Spacer(Modifier.height(10.dp))
            Text("اقدام فوری", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(situation?.immediateAction.orEmpty())
            Spacer(Modifier.height(10.dp))
            Text("تمرین روزانه", style = MaterialTheme.typography.titleMedium)
            Text(situation?.dailyPractice.orEmpty())
            Spacer(Modifier.height(12.dp))
            if (situation != null) {
                PrimaryActionButton("مشاهده آیه مرتبط") { onOpenAyah(situation.ayahId) }
            }
        }
    }
}
