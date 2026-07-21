package ir.ayeh.taamal.presentation.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard

data class MoreItem(val title: String, val onClick: () -> Unit)

@Composable
fun MoreScreen(
    onQuran: () -> Unit,
    onSearch: () -> Unit,
    onSituations: () -> Unit,
    onQuiz: () -> Unit,
    onScenarios: () -> Unit,
    onGames: () -> Unit,
    onReports: () -> Unit,
    onSettings: () -> Unit,
    onHelp: () -> Unit,
    onAbout: () -> Unit,
    onPrivacy: () -> Unit
) {
    val items = listOf(
        MoreItem("قرآن کریم", onQuran),
        MoreItem("جست‌وجوی موضوعی", onSearch),
        MoreItem("از موقعیت زندگی تا آیه", onSituations),
        MoreItem("آزمون‌ها", onQuiz),
        MoreItem("بازی‌های آموزشی", onGames),
        MoreItem("سناریوهای تصمیم‌گیری", onScenarios),
        MoreItem("گزارش پیشرفت", onReports),
        MoreItem("تنظیمات", onSettings),
        MoreItem("راهنمای برنامه", onHelp),
        MoreItem("درباره ما", onAbout),
        MoreItem("سیاست حریم خصوصی", onPrivacy)
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { SectionTitle("منوی بیشتر", "امکانات تکمیلی برنامه") }
        items(items) { item ->
            SoftCard(modifier = Modifier.clickable(onClick = item.onClick)) {
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}
