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
import androidx.compose.ui.graphics.vector.ImageVector
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
            Text(
                text = "سلام ${settings.userName}",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "امروز فرصتی تازه برای تدبر و عمل است",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = JalaliDate.todayFa(),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "${settings.streakDays} روز پیوسته",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SoftCard {
                SectionTitle(
                    title = "آیه روز",
                    subtitle = snap.ayah?.let {
                        "سوره ${it.surahNumber} آیه ${it.ayahNumber}"
                    }
                )

                if (snap.ayah != null) {
                    ArabicAyahText(
                        text = snap.ayah!!.arabicText
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = snap.translation,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    TextButton(
                        onClick = {
                            onOpenAyah(snap.ayah!!.id)
                        }
                    ) {
                        Text(
                            text = "مشاهده جزئیات"
                        )
                    }
                } else {
                    Text(
                        text = "در حال آماده‌سازی محتوا…"
                    )
                }
            }

            SoftCard {
                SectionTitle(
                    title = "پیام روز"
                )

                Text(
                    text = snap.message.ifBlank {
                        "هر آیه می‌تواند امروز یک رفتار بهتر بسازد."
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            SoftCard {
                SectionTitle(
                    title = "تمرین امروز"
                )

                val practice = snap.practice

                if (practice != null) {
                    Text(
                        text = practice.title,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = practice.description,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "${practice.durationMinutes} دقیقه · ${practice.difficulty}"
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    PrimaryActionButton(
                        text = "ثبت انجام تمرین",
                        onClick = {
                            viewModel.markPracticeDone()
                        }
                    )
                } else {
                    Text(
                        text = "تمرینی برای امروز یافت نشد."
                    )
                }
            }

            snap.path?.let { path ->
                SoftCard(
                    modifier = Modifier.clickable {
                        onOpenPath(path.id)
                    }
                ) {
                    SectionTitle(
                        title = "مسیر فعال",
                        subtitle = path.title
                    )

                    ProgressRow(
                        label = "پیشرفت",
                        progress = path.progress
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "${path.durationDays} روز · سطح ${path.level}"
                    )
                }
            }

            snap.challenge?.let { challenge ->
                SoftCard(
                    modifier = Modifier.clickable {
                        onOpenChallenge(challenge.id)
                    }
                ) {
                    SectionTitle(
                        title = "چالش فعال",
                        subtitle = challenge.title
                    )

                    Text(
                        text = challenge.instructions,
                        maxLines = 3,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "${challenge.durationDays} روز"
                    )
                }
            }

            SoftCard {
                SectionTitle(
                    title = "دسترسی سریع"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickItem(
                        label = "قرآن",
                        icon = Icons.Outlined.MenuBook,
                        onClick = onOpenQuran
                    )

                    QuickItem(
                        label = "جست‌وجو",
                        icon = Icons.Outlined.Search,
                        onClick = onOpenSearch
                    )

                    QuickItem(
                        label = "آزمون",
                        icon = Icons.Outlined.Quiz,
                        onClick = onOpenQuiz
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickItem(
                        label = "دفتر",
                        icon = Icons.Outlined.BookmarkBorder,
                        onClick = onOpenJournal
                    )

                    QuickItem(
                        label = "موقعیت",
                        icon = Icons.Outlined.Spa,
                        onClick = onOpenSituations
                    )

                    QuickItem(
                        label = "بیشتر",
                        icon = Icons.Outlined.SelfImprovement,
                        onClick = onOpenMore
                    )
                }
            }

            SoftCard {
                SectionTitle(
                    title = "آمار هفتگی"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatChip(
                        label = "آیات",
                        value = "${snap.stats.ayahsRead}"
                    )

                    StatChip(
                        label = "تمرین",
                        value = "${snap.stats.practicesDone}"
                    )

                    StatChip(
                        label = "آزمون",
                        value = "${snap.stats.quizzesTaken}"
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatChip(
                        label = "امتیاز",
                        value = "${snap.stats.avgScore}"
                    )

                    StatChip(
                        label = "یادداشت",
                        value = "${snap.stats.notesCount}"
                    )

                    StatChip(
                        label = "استمرار",
                        value = "${snap.stats.streak}"
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
    }
}
