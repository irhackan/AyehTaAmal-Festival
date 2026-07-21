package ir.ayeh.taamal.presentation.paths

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
import ir.ayeh.taamal.domain.model.PathItem
import ir.ayeh.taamal.domain.model.PathStep
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.ProgressRow
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PathsViewModel @Inject constructor(repo: ContentRepository) : ViewModel() {
    val paths = repo.observePaths().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@HiltViewModel
class PathDetailViewModel @Inject constructor(private val repo: ContentRepository) : ViewModel() {
    private val _steps = MutableStateFlow<List<PathStep>>(emptyList())
    val steps: StateFlow<List<PathStep>> = _steps.asStateFlow()
    private val _path = MutableStateFlow<PathItem?>(null)
    val path: StateFlow<PathItem?> = _path.asStateFlow()
    private var currentPathId: Long = 0L

    fun load(pathId: Long) {
        currentPathId = pathId
        viewModelScope.launch {
            _steps.value = repo.getPathSteps(pathId)
        }
        viewModelScope.launch {
            repo.observePaths().collect { list ->
                _path.value = list.find { it.id == pathId }
            }
        }
    }

    fun advance() {
        viewModelScope.launch {
            val p = _path.value ?: return@launch
            val next = (p.progress + 0.15f).coerceAtMost(1f)
            repo.updatePathProgress(p.id, next)
            repo.bumpProgress("practices_done")
        }
    }
}

@Composable
fun PathsScreen(
    onOpenPath: (Long) -> Unit,
    viewModel: PathsViewModel = hiltViewModel()
) {
    val paths by viewModel.paths.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionTitle("مسیرهای قرآنی", "از مفهوم تا رفتار") }
        items(paths, key = { it.id }) { path ->
            SoftCard(modifier = Modifier.clickable { onOpenPath(path.id) }) {
                Text(path.title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text(path.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                ProgressRow("پیشرفت", path.progress)
                Spacer(Modifier.height(4.dp))
                Text("${path.durationDays} روز · ${path.audience} · ${path.level}")
            }
        }
    }
}

@Composable
fun PathDetailScreen(
    pathId: Long,
    onOpenAyah: (Long) -> Unit,
    viewModel: PathDetailViewModel = hiltViewModel()
) {
    val path by viewModel.path.collectAsStateWithLifecycle()
    val steps by viewModel.steps.collectAsStateWithLifecycle()
    androidx.compose.runtime.LaunchedEffect(pathId) { viewModel.load(pathId) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            SectionTitle(path?.title ?: "مسیر", path?.description)
            path?.let { ProgressRow("پیشرفت مسیر", it.progress) }
            Spacer(Modifier.height(8.dp))
            PrimaryActionButton("ادامه مرحله بعد", onClick = { viewModel.advance() })
        }
        items(steps, key = { it.id }) { step ->
            SoftCard {
                Text("${step.order}. ${step.title}", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
                Text(step.sampleStory, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(6.dp))
                Text("تمرین: ${step.practiceText}")
                Spacer(Modifier.height(4.dp))
                Text("تدبر: ${step.reflectionQuestion}")
                Spacer(Modifier.height(8.dp))
                Text(
                    "مشاهده آیه مرحله",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onOpenAyah(step.ayahId) }
                )
            }
        }
    }
}
