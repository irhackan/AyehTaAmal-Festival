package ir.ayeh.taamal.presentation.home

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.ayeh.taamal.ui.components.ArabicAyahText
import ir.ayeh.taamal.ui.components.GradientHeader
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.ProgressRow
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import ir.ayeh.taamal.ui.components.StatChip
import ir.ayeh.taamal.utility.JalaliDate

@Composable
fun HomeScreen(
    onOpenAyah: (Long) -> Unit,
    onOpenPath: (Long) -> Unit,
    onOpenChallenge: (Long) -> Unit,
    onOpenQuran: () -> Unit,
    onOpenSearch: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenJournal: () -> Unit,
    onOpenSituations: () -> Unit,
    onOpenMore: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val snap by viewModel.snapshot.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GradientHeader {
            Text("سلام ${settings.userName}", style = MaterialTheme.typography.headlineMedium)
            Text(
                "امروز فرصتی تازه برای تدبر و عمل است",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(JalaliDate.todayFa(), style = MaterialTheme.typography.bodyMedium)
                Text(
                    "${settings.streakDays} روز پیوسته",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SoftCard {
                SectionTitle("آیه روز", snap.ayah?.let { "سوره ${it.surahNumber} آیه ${it.ayahNumber}" })
                if (snap.ayah != null) {
                    ArabicAyahText(snap.ayah!!.arabicText)
                    Spacer(Modifier.height(10.dp))
                    Text(snap.translation, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = { onOpenAyah(snap.ayah!!.id) }) {
                        Text("مشاهده جزئیات")
                    }
                } else {
                    Text("در حال آماده‌سازی محتوا…")
                }
            }

            SoftCard {
                SectionTitle("پیام روز")
                Text(
                    snap.message.ifBlank { "هر آیه می‌تواند امروز یک رفتار بهتر بسازد." },
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            SoftCard {
                SectionTitle("تمرین امروز")
                val practice = snap.practice
                if (practice != null) {
                    Text(practice.title, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(practice.description, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(6.dp))
                    Text("${practice.durationMinutes} دقیقه · ${practice.difficulty}")
                    Spacer(Modifier.height(10.dp))
                    PrimaryActionButton("ثبت انجام تمرین", onClick = { viewModel.markPracticeDone() })
                } else {
                    Text("تمرینی برای امروز یافت نشد.")
                }
            }

            snap.path?.let { path ->
                SoftCard(modifier = Modifier.clickable { onOpenPath(path.id) }) {
                    SectionTitle("مسیر فعال", path.title)
                    ProgressRow("پیشرفت", path.progress)
                    Spacer(Modifier.height(8.dp))
                    Text("${path.durationDays} روز · سطح ${path.level}")
                }
            }

            snap.challenge?.let { ch ->
                SoftCard(modifier = Modifier.clickable { onOpenChallenge(ch.id) }) {
                    SectionTitle("چالش فعال", ch.title)
                    Text(ch.instructions, maxLines = 3, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(6.dp))
                    Text("${ch.durationDays} روز")
                }
            }

            SoftCard {
                SectionTitle("دسترسی سریع")
                Row(Modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    QuickItem("قرآن", Icons.Outlined.MenuBook, onOpenQuran)
                    QuickItem("جست‌وجو", Icons.Outlined.Search, onOpenSearch)
                    QuickItem("آزمون", Icons.Outlined.Quiz, onOpenQuiz)
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    QuickItem("دفتر", Icons.Outlined.BookmarkBorder, onOpenJournal)
                    QuickItem("موقعیت", Icons.Outlined.Spa, onOpenSituations)
                    QuickItem("بیشتر", Icons.Outlined.SelfImprovement, onOpenMore)
                }
            }

            SoftCard {
                SectionTitle("آمار هفتگی")
                Row(Modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatChip("آیات", "${snap.stats.ayahsRead}")
                    StatChip("تمرین", "${snap.stats.practicesDone}")
                    StatChip("آزمون", "${snap.stats.quizzesTaken}")
                }
                Spacer(Modifier.height(8.dp))
                Row(Modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatChip("امتیاز", "${snap.stats.avgScore}")
                    StatChip("یادداشت", "${snap.stats.notesCount}")
                    StatChip("استمرار", "${snap.stats.streak}")
                }
            }
        }
    }
}

@Composable
private fun QuickItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}
