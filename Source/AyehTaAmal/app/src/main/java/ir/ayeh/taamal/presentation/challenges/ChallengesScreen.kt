package ir.ayeh.taamal.presentation.challenges

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.ChallengeItem
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.ProgressRow
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ChallengesViewModel @Inject constructor(
    private val repo: ContentRepository,
    private val prefs: UserPreferencesRepository
) : ViewModel() {
    val challenges = repo.observeChallenges()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun completeDay() {
        viewModelScope.launch {
            repo.bumpProgress("practices_done")
            prefs.addPoints(10)
        }
    }
}

@Composable
fun ChallengesScreen(
    onOpenChallenge: (Long) -> Unit,
    viewModel: ChallengesViewModel = hiltViewModel()
) {
    val list by viewModel.challenges.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionTitle("چالش‌های عملی", "تغییر رفتار در چند روز") }
        items(list, key = { it.id }) { item ->
            SoftCard(modifier = Modifier.clickable { onOpenChallenge(item.id) }) {
                Text(item.title, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
                Text(item.instructions, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 3)
                Spacer(Modifier.height(6.dp))
                Text("${item.durationDays} روز")
            }
        }
    }
}

@Composable
fun ChallengeDetailScreen(
    challenge: ChallengeItem?,
    onOpenAyah: (Long) -> Unit,
    onCompleteDay: () -> Unit
) {
    var day by remember { mutableIntStateOf(1) }
    val total = challenge?.durationDays ?: 7
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(challenge?.title ?: "چالش", "روز $day از $total")
        SoftCard {
            Text(challenge?.instructions.orEmpty())
            Spacer(Modifier.height(10.dp))
            ProgressRow("پیشرفت", day.toFloat() / total)
            Spacer(Modifier.height(10.dp))
            PrimaryActionButton("ثبت انجام امروز") {
                if (day < total) day++
                onCompleteDay()
            }
            Spacer(Modifier.height(8.dp))
            if (challenge != null) {
                Text(
                    "مشاهده آیه محوری",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onOpenAyah(challenge.ayahId) }
                )
            }
        }
    }
}
