package ir.ayeh.taamal.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.ayeh.taamal.domain.model.UserSettings
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.SoftCard
import ir.ayeh.taamal.ui.theme.AppColorScheme

@Composable
fun OnboardingScreen(
    settings: UserSettings,
    onUpdate: (UserSettings) -> Unit,
    onFinished: () -> Unit
) {
    var page by remember { mutableIntStateOf(0) }
    var audience by remember { mutableStateOf(settings.audienceType) }
    var colorKey by remember { mutableStateOf(settings.colorScheme) }
    var nightMode by remember { mutableStateOf(settings.nightMode) }
    var fontScale by remember { mutableFloatStateOf(settings.arabicFontScale) }
    var translatorId by remember { mutableStateOf(settings.translatorId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) { i ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (i == page) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (i == page) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (page) {
                0 -> IntroPage(
                    title = "آیه را بخوان",
                    body = "هر روز یک آیه را همراه با ترجمه، صوت و منابع معتبر مطالعه کنید."
                )
                1 -> IntroPage(
                    title = "آیه را بفهم",
                    body = "پیام آیه، واژگان مهم و کاربرد آن را در زندگی امروز مشاهده کنید."
                )
                2 -> IntroPage(
                    title = "آیه را عمل کن",
                    body = "مفاهیم قرآن را به تمرین‌های ساده و قابل اجرا تبدیل کنید."
                )
                3 -> {
                    Text("مسیر خود را انتخاب کن", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "بر اساس نقش شما، محتوای مناسب‌تری پیشنهاد می‌شود.",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))
                    SoftCard {
                        listOf(
                            "general" to "عمومی",
                            "student" to "دانشجو",
                            "teen" to "نوجوان",
                            "family" to "خانواده"
                        ).forEach { (key, label) ->
                            FilterChip(
                                selected = audience == key,
                                onClick = { audience = key },
                                label = { Text(label) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }
                else -> {
                    Text("تنظیمات اولیه", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(12.dp))
                    SoftCard {
                        Text("مترجم پیش‌فرض", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        listOf(1L to "فولادوند", 2L to "مکارم شیرازی", 3L to "الهی قمشه‌ای")
                            .forEach { (id, name) ->
                                FilterChip(
                                    selected = translatorId == id,
                                    onClick = { translatorId = id },
                                    label = { Text(name) },
                                    modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                                )
                            }
                        Spacer(Modifier.height(12.dp))
                        Text("رنگ‌بندی", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        AppColorScheme.entries.take(5).forEach { scheme ->
                            FilterChip(
                                selected = colorKey == scheme.key,
                                onClick = { colorKey = scheme.key },
                                label = { Text(scheme.titleFa) },
                                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("حالت نمایش", fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(8.dp))
                        listOf("system" to "خودکار", "light" to "روشن", "dark" to "شب").forEach { (k, l) ->
                            FilterChip(
                                selected = nightMode == k,
                                onClick = { nightMode = k },
                                label = { Text(l) },
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("اندازه قلم عربی: ${"%.1f".format(fontScale)}")
                        Slider(
                            value = fontScale,
                            onValueChange = { fontScale = it },
                            valueRange = 0.9f..1.6f
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        PrimaryActionButton(
            text = if (page < 4) "ادامه" else "ورود به برنامه",
            onClick = {
                if (page < 4) {
                    page++
                } else {
                    onUpdate(
                        settings.copy(
                            audienceType = audience,
                            colorScheme = colorKey,
                            nightMode = nightMode,
                            arabicFontScale = fontScale,
                            translatorId = translatorId,
                            onboardingDone = true
                        )
                    )
                    onFinished()
                }
            }
        )
        if (page > 0) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "بازگشت",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { page-- }
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun IntroPage(title: String, body: String) {
    SoftCard {
        Text(title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        Text(
            body,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text("آ", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}
