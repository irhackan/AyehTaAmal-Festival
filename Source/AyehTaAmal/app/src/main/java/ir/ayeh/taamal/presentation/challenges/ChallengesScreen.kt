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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

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
        item {
            SectionTitle(
                title = "چالش‌های عملی",
                subtitle = "تغییر رفتار در چند روز"
            )
        }

        items(
            items = list,
            key = { it.id }
        ) { challengeItem ->
            SoftCard(
                modifier = Modifier.clickable {
                    onOpenChallenge(challengeItem.id)
                }
            ) {
                Text(
                    text = challengeItem.title,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = challengeItem.instructions,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "${challengeItem.durationDays} روز"
                )
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
    var day by remember {
        mutableIntStateOf(1)
    }

    val total = challenge?.durationDays ?: 7

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(
            title = challenge?.title ?: "چالش",
            subtitle = "روز $day از $total"
        )

        SoftCard {
            Text(
                text = challenge?.instructions.orEmpty()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            ProgressRow(
                label = "پیشرفت",
                progress = day.toFloat() / total.toFloat()
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            PrimaryActionButton(
                text = "ثبت انجام امروز",
                onClick = {
                    if (day < total) {
                        day++
                    }
                    onCompleteDay()
                }
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            if (challenge != null) {
                Text(
                    text = "مشاهده آیه محوری",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onOpenAyah(challenge.ayahId)
                    }
                )
            }
        }
    }
}
