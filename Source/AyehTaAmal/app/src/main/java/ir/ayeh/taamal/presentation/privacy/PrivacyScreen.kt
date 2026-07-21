package ir.ayeh.taamal.presentation.privacy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.ayeh.taamal.ui.components.SectionTitle
import ir.ayeh.taamal.ui.components.SoftCard

@Composable
fun PrivacyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle("سیاست حریم خصوصی")
        SoftCard {
            Text("۱. داده‌های شخصی", fontWeight = FontWeight.SemiBold)
            Text("یادداشت‌ها، تنظیمات و پیشرفت شما فقط روی دستگاه ذخیره می‌شود و بدون رضایت شما ارسال نمی‌گردد.")
        }
        SoftCard {
            Text("۲. مجوزها", fontWeight = FontWeight.SemiBold)
            Text("برنامه فقط مجوزهای ضروری (اینترنت برای به‌روزرسانی اختیاری) را درخواست می‌کند. به مخاطبان، پیامک یا موقعیت مکانی دسترسی ندارد.")
        }
        SoftCard {
            Text("۳. حذف داده", fontWeight = FontWeight.SemiBold)
            Text("می‌توانید از بخش تنظیمات، داده‌های محلی را پاک کنید یا برنامه را حذف نمایید.")
        }
        SoftCard {
            Text("۴. تماس", fontWeight = FontWeight.SemiBold)
            Text("برای سؤالات حریم خصوصی: irhackan@gmail.com")
        }
    }
}
