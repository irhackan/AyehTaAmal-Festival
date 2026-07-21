package ir.ayeh.taamal.presentation.help

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
fun HelpScreen() {
    val items = listOf(
        "اجرای اولیه" to "پس از نصب، معرفی برنامه را طی کنید و نوع کاربر، مترجم و رنگ‌بندی را انتخاب نمایید.",
        "مطالعه قرآن" to "از منوی بیشتر یا دسترسی سریع خانه وارد فهرست سوره‌ها شوید. آیات منتخب دارای محتوای تدبری کامل هستند.",
        "مسیرها" to "یک مسیر موضوعی انتخاب کنید، مراحل را بخوانید و پیشرفت خود را ثبت کنید.",
        "چالش‌ها" to "چالش چندروزه را آغاز کنید و هر روز انجام مأموریت را ثبت نمایید.",
        "دفتر تدبر" to "برداشت، تصمیم و نتیجه تمرین را در دفتر ذخیره کنید.",
        "آزمون" to "با آزمون‌های کوتاه مفاهیم را مرور کنید.",
        "تنظیمات" to "حالت شب، رنگ‌بندی، اندازه قلم و مترجم را از بخش تنظیمات تغییر دهید.",
        "پشتیبان‌گیری" to "در این نسخه داده‌ها روی دستگاه ذخیره می‌شوند؛ پشتیبان ابری در نسخه بعدی اضافه می‌شود."
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SectionTitle("راهنمای برنامه")
        items.forEach { (title, body) ->
            SoftCard {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(body)
            }
        }
    }
}
