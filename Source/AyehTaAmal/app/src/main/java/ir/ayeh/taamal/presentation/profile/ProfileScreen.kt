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
class ProfileViewModel @Inject constructor(private val prefs: UserPreferencesRepository) : ViewModel() {
    val settings = prefs.settings.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserSettings())

    fun updateName(name: String) {
        viewModelScope.launch { prefs.update { it.copy(userName = name) } }
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
    var name by remember(settings.userName) { mutableStateOf(settings.userName) }
    val badges = buildBadges(settings)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        GradientHeader {
            Text(settings.userName, style = MaterialTheme.typography.headlineMedium)
            Text(audienceLabel(settings.audienceType), color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Row(Modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatChip("امتیاز", "${settings.totalPoints}")
                StatChip("استمرار", "${settings.streakDays}")
                StatChip("سطح", levelFromPoints(settings.totalPoints))
            }
        }
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SoftCard {
                SectionTitle("اطلاعات کاربری")
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("نام") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                PrimaryActionButton("ذخیره نام") { viewModel.updateName(name.ifBlank { "مهمان" }) }
            }
            SoftCard {
                SectionTitle("نشان‌ها")
                badges.forEach { Text("• $it", modifier = Modifier.padding(vertical = 2.dp)) }
            }
            SoftCard {
                MenuRow("تنظیمات", onSettings)
                MenuRow("گزارش پیشرفت", onReports)
                MenuRow("راهنمای برنامه", onHelp)
                MenuRow("درباره ما", onAbout)
            }
        }
    }
}

@Composable
private fun MenuRow(title: String, onClick: () -> Unit) {
    Text(
        title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

private fun audienceLabel(key: String) = when (key) {
    "student" -> "حالت دانشجو"
    "teen" -> "حالت نوجوان"
    "family" -> "حالت خانواده"
    else -> "حالت عمومی"
}

private fun levelFromPoints(p: Int) = when {
    p >= 500 -> "پیشرفته"
    p >= 150 -> "متوسط"
    else -> "آغازگر"
}

private fun buildBadges(s: UserSettings): List<String> {
    val list = mutableListOf("آغازگر تدبر")
    if (s.streakDays >= 7) list += "هفت روز استمرار"
    if (s.streakDays >= 30) list += "سی روز استمرار"
    if (s.totalPoints >= 50) list += "همراه قرآن"
    if (s.totalPoints >= 100) list += "کاربر فعال"
    if (s.audienceType == "student") list += "دانشجوی مسئول"
    if (s.audienceType == "family") list += "یاور خانواده"
    return list
}
