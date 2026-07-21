package ir.ayeh.taamal.presentation.profile

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.domain.model.UserSettings
import ir.ayeh.taamal.ui.components.GradientHeader
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard
import ir.ayeh.taamal.ui.components.StatChip
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefs: UserPreferencesRepository
) : ViewModel() {

    val settings = prefs.settings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserSettings()
    )

    fun updateName(name: String) {
        viewModelScope.launch {
            prefs.update {
                it.copy(userName = name)
            }
        }
    }
}

@Composable
fun ProfileScreen(
    onSettings: () -> Unit,
    onAbout: () -> Unit,
    onHelp: () -> Unit,
    onReports: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    var name by remember(settings.userName) {
        mutableStateOf(settings.userName)
    }

    val badges = buildBadges(settings)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GradientHeader {
            Text(
                text = settings.userName,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = audienceLabel(settings.audienceType),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatChip(
                    title = "امتیاز",
                    value = "${settings.totalPoints}"
                )

                StatChip(
                    title = "استمرار",
                    value = "${settings.streakDays}"
                )

                StatChip(
                    title = "سطح",
                    value = levelFromPoints(settings.totalPoints)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SoftCard {
                SectionTitle(
                    title = "اطلاعات کاربری"
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    label = {
                        Text(
                            text = "نام"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                PrimaryActionButton(
                    text = "ذخیره نام",
                    onClick = {
                        viewModel.updateName(
                            name.ifBlank {
                                "مهمان"
                            }
                        )
                    }
                )
            }

            SoftCard {
                SectionTitle(
                    title = "نشان‌ها"
                )

                badges.forEach { badge ->
                    Text(
                        text = "• $badge",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }

            SoftCard {
                MenuRow(
                    title = "تنظیمات",
                    onClick = onSettings
                )

                MenuRow(
                    title = "گزارش پیشرفت",
                    onClick = onReports
                )

                MenuRow(
                    title = "راهنمای برنامه",
                    onClick = onHelp
                )

                MenuRow(
                    title = "درباره ما",
                    onClick = onAbout
                )
            }
        }
    }
}

@Composable
private fun MenuRow(
    title: String,
    onClick: () -> Unit
) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

private fun audienceLabel(key: String): String {
    return when (key) {
        "student" -> "حالت دانشجو"
        "teen" -> "حالت نوجوان"
        "family" -> "حالت خانواده"
        else -> "حالت عمومی"
    }
}

private fun levelFromPoints(points: Int): String {
    return when {
        points >= 500 -> "پیشرفته"
        points >= 150 -> "متوسط"
        else -> "آغازگر"
    }
}

private fun buildBadges(settings: UserSettings): List<String> {
    val badges = mutableListOf(
        "آغازگر تدبر"
    )

    if (settings.streakDays >= 7) {
        badges += "هفت روز استمرار"
    }

    if (settings.streakDays >= 30) {
        badges += "سی روز استمرار"
    }

    if (settings.totalPoints >= 50) {
        badges += "همراه قرآن"
    }

    if (settings.totalPoints >= 100) {
        badges += "کاربر فعال"
    }

    if (settings.audienceType == "student") {
        badges += "دانشجوی مسئول"
    }

    if (settings.audienceType == "family") {
        badges += "یاور خانواده"
    }

    return badges
}
