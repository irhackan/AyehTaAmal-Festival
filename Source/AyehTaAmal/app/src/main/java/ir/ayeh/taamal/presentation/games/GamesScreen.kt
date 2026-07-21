package ir.ayeh.taamal.presentation.games

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.QuizQuestion
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class GamesViewModel @Inject constructor(private val repo: ContentRepository) : ViewModel() {
    var questions by mutableStateOf<List<QuizQuestion>>(emptyList())
        private set

    fun load() {
        viewModelScope.launch { questions = repo.randomQuiz(5) }
    }
}

@Composable
fun GamesScreen(viewModel: GamesViewModel = hiltViewModel()) {
    var started by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var picked by remember { mutableIntStateOf(-1) }
    val questions = viewModel.questions

    LaunchedEffect(started) {
        if (started) viewModel.load()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle("بازی‌های آموزشی", "تطبیق مفهوم و آیه")

        if (!started) {
            SoftCard {
                Text("بازی «انتخاب پاسخ»", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Text("۵ سؤال کوتاه درباره مفاهیم قرآنی. هر پاسخ صحیح ۵ امتیاز دارد.")
                Spacer(Modifier.height(12.dp))
                PrimaryActionButton("شروع بازی") { started = true }
            }
            return@Column
        }

        if (questions.isEmpty()) {
            SoftCard { Text("در حال بارگذاری…") }
            return@Column
        }

        if (finished) {
            SoftCard {
                Text("پایان بازی!", style = MaterialTheme.typography.headlineMedium)
                Text("امتیاز: $score از ${questions.size * 5}")
                Spacer(Modifier.height(12.dp))
                PrimaryActionButton("بازی دوباره") {
                    finished = false
                    index = 0
                    score = 0
                    answered = false
                    picked = -1
                    viewModel.load()
                }
            }
            return@Column
        }

        val q = questions[index]
        SoftCard {
            Text("سؤال ${index + 1} از ${questions.size}")
            Spacer(Modifier.height(8.dp))
            Text(q.question, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            q.options.forEachIndexed { i, opt ->
                SoftCard(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable(enabled = !answered) {
                            picked = i
                            answered = true
                            if (i == q.correctIndex) score += 5
                        }
                ) {
                    Text(
                        opt,
                        color = when {
                            !answered -> MaterialTheme.colorScheme.onSurface
                            i == q.correctIndex -> MaterialTheme.colorScheme.primary
                            picked == i -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
            if (answered) {
                Spacer(Modifier.height(8.dp))
                Text(q.explanation, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                PrimaryActionButton(
                    text = if (index < questions.lastIndex) "سؤال بعد" else "پایان"
                ) {
                    if (index < questions.lastIndex) {
                        index++
                        answered = false
                        picked = -1
                    } else {
                        finished = true
                    }
                }
            }
        }
    }
}

private fun Modifier.padding(bottom: androidx.compose.ui.unit.Dp) =
    this.then(androidx.compose.foundation.layout.padding(bottom = bottom))
