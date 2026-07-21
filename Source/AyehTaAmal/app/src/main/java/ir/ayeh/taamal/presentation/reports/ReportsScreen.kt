package ir.ayeh.taamal.presentation.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.UserSettings
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import ir.ayeh.taamal.ui.components.StatChip
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repo: ContentRepository,
    prefs: UserPreferencesRepository
) : ViewModel() {
    val settings = prefs.settings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())
    var stats by mutableStateOf(
        ir.ayeh.taamal.domain.model.WeeklyStats()
    )
        private set

    init {
        viewModelScope.launch {
            stats = repo.homeSnapshot().stats
        }
    }
}

@Composable
fun ReportsScreen(viewModel: ReportsViewModel = hiltViewModel()) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val stats = viewModel.stats

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle("گزارش پیشرفت", "خلاصه فعالیت شما")
        SoftCard {
            Text("کاربر: ${settings.userName}", fontWeight = FontWeight.SemiBold)
            Text("امتیاز کل: ${settings.totalPoints}")
            Text("استمرار: ${settings.streakDays} روز")
        }
        SoftCard {
            SectionTitle("گزارش هفتگی")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatChip("آیات", "${stats.ayahsRead}")
                StatChip("تمرین", "${stats.practicesDone}")
                StatChip("آزمون", "${stats.quizzesTaken}")
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatChip("میانگین", "${stats.avgScore}")
                StatChip("یادداشت", "${stats.notesCount}")
                StatChip("پیوستگی", "${stats.streak}")
            }
        }
        SoftCard {
            SectionTitle("پیشنهاد مرحله بعد")
            Text(
                when {
                    stats.practicesDone < 3 -> "امروز یک تمرین عملی از صفحه خانه انجام دهید."
                    stats.quizzesTaken < 2 -> "یک آزمون کوتاه بزنید تا مفاهیم را مرور کنید."
                    else -> "یک مسیر قرآنی را ادامه دهید و پیشرفت خود را ثبت نمایید."
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
