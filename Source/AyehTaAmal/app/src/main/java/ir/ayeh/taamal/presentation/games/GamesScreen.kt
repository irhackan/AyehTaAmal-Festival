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
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val repo: ContentRepository
) : ViewModel() {

    var questions by mutableStateOf<List<QuizQuestion>>(emptyList())
        private set

    fun load() {
        viewModelScope.launch {
            questions = repo.randomQuiz(5)
        }
    }
}

@Composable
fun GamesScreen(
    viewModel: GamesViewModel = hiltViewModel()
) {
    var started by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var index by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var picked by remember { mutableIntStateOf(-1) }

    val questions = viewModel.questions

    LaunchedEffect(started) {
        if (started) {
            viewModel.load()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(
            title = "بازی‌های آموزشی",
            subtitle = "تطبیق مفهوم و آیه"
        )

        if (!started) {
            SoftCard {
                Text(
                    text = "بازی «انتخاب پاسخ»",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "۵ سؤال کوتاه درباره مفاهیم قرآنی. هر پاسخ صحیح ۵ امتیاز دارد."
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                PrimaryActionButton(
                    text = "شروع بازی",
                    onClick = {
                        started = true
                    }
                )
            }

            return@Column
        }

        if (questions.isEmpty()) {
            SoftCard {
                Text(
                    text = "در حال بارگذاری…"
                )
            }

            return@Column
        }

        if (finished) {
            SoftCard {
                Text(
                    text = "پایان بازی!",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "امتیاز: $score از ${questions.size * 5}"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                PrimaryActionButton(
                    text = "بازی دوباره",
                    onClick = {
                        finished = false
                        index = 0
                        score = 0
                        answered = false
                        picked = -1
                        viewModel.load()
                    }
                )
            }

            return@Column
        }

        val question = questions[index]

        SoftCard {
            Text(
                text = "سؤال ${index + 1} از ${questions.size}"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            question.options.forEachIndexed { optionIndex, option ->
                SoftCard(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable(
                            enabled = !answered
                        ) {
                            picked = optionIndex
                            answered = true

                            if (optionIndex == question.correctIndex) {
                                score += 5
                            }
                        }
                ) {
                    Text(
                        text = option,
                        color = when {
                            !answered ->
                                MaterialTheme.colorScheme.onSurface

                            optionIndex == question.correctIndex ->
                                MaterialTheme.colorScheme.primary

                            picked == optionIndex ->
                                MaterialTheme.colorScheme.error

                            else ->
                                MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            if (answered) {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = question.explanation,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                PrimaryActionButton(
                    text = if (index < questions.lastIndex) {
                        "سؤال بعد"
                    } else {
                        "پایان"
                    },
                    onClick = {
                        if (index < questions.lastIndex) {
                            index++
                            answered = false
                            picked = -1
                        } else {
                            finished = true
                        }
                    }
                )
            }
        }
    }
}
