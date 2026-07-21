package ir.ayeh.taamal.presentation.quiz

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.domain.model.QuizQuestion
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repo: ContentRepository,
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    var questions by mutableStateOf<List<QuizQuestion>>(emptyList())
        private set

    fun load() {
        viewModelScope.launch {
            questions = repo.randomQuiz(8)
        }
    }

    fun recordScore(correct: Int) {
        viewModelScope.launch {
            repo.bumpProgress("quizzes_taken")
            prefs.addPoints(correct * 5)
        }
    }
}

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.load()
    }

    val questions = viewModel.questions

    var index by remember {
        mutableIntStateOf(0)
    }

    var selected by remember {
        mutableIntStateOf(-1)
    }

    var correctCount by remember {
        mutableIntStateOf(0)
    }

    var finished by remember {
        mutableStateOf(false)
    }

    var showAnswer by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(
            title = "آزمون روز",
            subtitle = if (questions.isEmpty()) {
                "در حال بارگذاری…"
            } else {
                "سؤال ${index + 1} از ${questions.size}"
            }
        )

        if (finished) {
            SoftCard {
                Text(
                    text = "پایان آزمون",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "پاسخ صحیح: $correctCount از ${questions.size}"
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                PrimaryActionButton(
                    text = "آزمون دوباره",
                    onClick = {
                        index = 0
                        selected = -1
                        correctCount = 0
                        finished = false
                        showAnswer = false
                        viewModel.load()
                    }
                )
            }

            return@Column
        }

        val question = questions.getOrNull(index) ?: return@Column

        SoftCard {
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            question.options.forEachIndexed { optionIndex, option ->
                SoftCard(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable(
                            enabled = !showAnswer,
                            onClick = {
                                selected = optionIndex
                            }
                        )
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.fillMaxWidth(),
                        color = when {
                            !showAnswer && selected == optionIndex ->
                                MaterialTheme.colorScheme.primary

                            showAnswer && optionIndex == question.correctIndex ->
                                MaterialTheme.colorScheme.primary

                            showAnswer &&
                                selected == optionIndex &&
                                optionIndex != question.correctIndex ->
                                MaterialTheme.colorScheme.error

                            else ->
                                MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }

            if (showAnswer) {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = question.explanation,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "منبع: ${question.source}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            PrimaryActionButton(
                text = when {
                    !showAnswer -> "ثبت پاسخ"
                    index < questions.lastIndex -> "سؤال بعد"
                    else -> "مشاهده نتیجه"
                },
                enabled = selected >= 0 || showAnswer,
                onClick = {
                    if (!showAnswer) {
                        showAnswer = true

                        if (selected == question.correctIndex) {
                            correctCount++
                        }
                    } else if (index < questions.lastIndex) {
                        index++
                        selected = -1
                        showAnswer = false
                    } else {
                        finished = true
                        viewModel.recordScore(correctCount)
                    }
                }
            )
        }
    }
}
