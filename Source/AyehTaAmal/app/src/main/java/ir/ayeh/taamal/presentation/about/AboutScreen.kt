package ir.ayeh.taamal.presentation.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.ayeh.taamal.ui.components.PrimaryActionButton
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard

@Composable
fun AboutScreen(onPrivacy: () -> Unit = {}) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle("درباره ما", "آیه تا عمل")
        SoftCard {
            Text(
                "اپلیکیشن «آیه تا عمل» با هدف تقویت ارتباط کاربردی کاربران با قرآن کریم طراحی شده است. " +
                    "این برنامه تلاش می‌کند مفاهیم قرآن را از سطح مطالعه و آشنایی نظری به سطح تدبر، " +
                    "تصمیم‌گیری و تمرین‌های رفتاری روزانه منتقل کند."
            )
            Spacer(Modifier.height(10.dp))
            Text(
                "کاربران می‌توانند آیات قرآن را همراه با ترجمه‌های معتبر، نکات تفسیری، کاربردهای امروزی، " +
                    "تمرین‌های عملی، آزمون‌ها و مسیرهای آموزشی مطالعه کنند و روند پیشرفت خود را مشاهده نمایند."
            )
        }
        SoftCard {
            Text("اطلاعات اثر", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            InfoLine("نام برنامه", "آیه تا عمل")
            InfoLine("عنوان اثر", "اپلیکیشن تدبر، تمرین و سبک زندگی قرآنی")
            InfoLine("شعار", "از تلاوت تا تغییر رفتار")
            InfoLine("نسخه", "۱.۰")
            InfoLine("سال تولید", "۱۴۰۵")
        }
        SoftCard {
            Text("اطلاعات سازنده", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            InfoLine("طراح و برنامه‌نویس", "کاظم دهناد")
            InfoLine("شماره تماس", "09359654067")
            InfoLine("ایمیل", "irhackan@gmail.com")
            Spacer(Modifier.height(12.dp))
            PrimaryActionButton("تماس تلفنی") {
                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:09359654067")))
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onPrivacy) { Text("سیاست حریم خصوصی") }
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = {
                context.startActivity(
                    Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:irhackan@gmail.com"))
                )
            }) { Text("ارسال ایمیل") }
        }
    }
}

@Composable
private fun InfoLine(label: String, value: String) {
    Text("$label: $value", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 2.dp))
}
