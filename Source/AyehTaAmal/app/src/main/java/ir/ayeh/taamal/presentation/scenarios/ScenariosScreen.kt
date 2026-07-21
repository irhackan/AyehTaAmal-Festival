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
class ScenariosViewModel @Inject constructor(
    repo: ContentRepository
) : ViewModel() {

    val scenarios = repo.observeScenarios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

@Composable
fun ScenariosScreen(
    onOpenScenario: (Long) -> Unit,
    viewModel: ScenariosViewModel = hiltViewModel()
) {
    val scenarios by viewModel.scenarios.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            SectionTitle(
                title = "سناریوهای تصمیم‌گیری"
            )
        }

        items(
            items = scenarios,
            key = { it.id }
        ) { scenario ->
            SoftCard(
                modifier = Modifier.clickable {
                    onOpenScenario(scenario.id)
                }
            ) {
                Text(
                    text = scenario.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = scenario.situation,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ScenarioDetailScreen(
    scenario: ScenarioItem?,
    onOpenAyah: (Long) -> Unit
) {
    var selected by remember {
        mutableIntStateOf(-1)
    }

    var revealed by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(
            title = scenario?.title ?: "سناریو"
        )

        SoftCard {
            Text(
                text = scenario?.situation.orEmpty()
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            scenario?.options?.forEachIndexed { index, option ->
                SoftCard(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable {
                            selected = index
                            revealed = true
                        }
                ) {
                    Text(
                        text = "${index + 1}. $option"
                    )
                }
            }

            if (revealed && selected >= 0) {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "پیامد: ${
                        scenario?.outcomes
                            ?.getOrNull(selected)
                            .orEmpty()
                    }",
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "تحلیل: ${scenario?.analysis.orEmpty()}"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                if (scenario != null) {
                    PrimaryActionButton(
                        text = "آیه مرتبط",
                        onClick = {
                            onOpenAyah(scenario.relatedAyahId)
                        }
                    )
                }
            }
        }
    }
}
