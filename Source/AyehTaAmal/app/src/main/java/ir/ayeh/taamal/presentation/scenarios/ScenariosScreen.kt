package ir.ayeh.taamal.presentation.scenarios

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.ScenarioItem
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ScenariosViewModel @Inject constructor(repo: ContentRepository) : ViewModel() {
    val scenarios = repo.observeScenarios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@Composable
fun ScenariosScreen(
    onOpenScenario: (Long) -> Unit,
    viewModel: ScenariosViewModel = hiltViewModel()
) {
    val list by viewModel.scenarios.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { SectionTitle("سناریوهای تصمیم‌گیری") }
        items(list, key = { it.id }) { item ->
            SoftCard(modifier = Modifier.clickable { onOpenScenario(item.id) }) {
                Text(item.title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text(item.situation, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun ScenarioDetailScreen(
    scenario: ScenarioItem?,
    onOpenAyah: (Long) -> Unit
) {
    var selected by remember { mutableIntStateOf(-1) }
    var revealed by remember { mutableStateOf(false) }
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(scenario?.title ?: "سناریو")
        SoftCard {
            Text(scenario?.situation.orEmpty())
            Spacer(Modifier.height(12.dp))
            scenario?.options?.forEachIndexed { index, option ->
                SoftCard(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable {
                            selected = index
                            revealed = true
                        }
                ) {
                    Text("${index + 1}. $option")
                }
            }
            if (revealed && selected >= 0) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "پیامد: ${scenario?.outcomes?.getOrNull(selected).orEmpty()}",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(8.dp))
                Text("تحلیل: ${scenario?.analysis.orEmpty()}")
                Spacer(Modifier.height(12.dp))
                if (scenario != null) {
                    PrimaryActionButton("آیه مرتبط") { onOpenAyah(scenario.relatedAyahId) }
                }
            }
        }
    }
}
