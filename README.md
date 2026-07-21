# آیه تا عمل — Ayeh Ta Amal

اپلیکیشن اندرویدی **تدبر، تمرین و سبک زندگی قرآنی**

| | |
|---|---|
| **نسخه** | 1.0 |
| **سازنده** | کاظم دهناد |
| **ایمیل** | irhackan@gmail.com |
| **تلفن** | 09359654067 |

---

## آپلود در GitHub و دریافت APK

### ۱. ایجاد مخزن GitHub
```bash
git init
git add .
git commit -m "آیه تا عمل v1.0"
git branch -M main
git remote add origin https://github.com/USERNAME/AyehTaAmal-Festival.git
git push -u origin main
```

### ۲. ساخت خودکار APK
پس از push، GitHub Actions به‌صورت خودکار APK می‌سازد:

1. به مخزن GitHub بروید
2. تب **Actions** → workflow **Build Android APK**
3. پس از اتمام (✓ سبز)، روی Run کلیک کنید
4. در پایین صفحه، بخش **Artifacts** → دانلود:
   - `AyehTaAmal-v1.0-debug` (برای تست)
   - `AyehTaAmal-v1.0-release` (نسخه انتشار)

### ۳. ساخت دستی (Actions)
Actions → Build Android APK → **Run workflow**

---

## ساخت محلی (Android Studio)

1. Android Studio را باز کنید
2. پوشه `Source/AyehTaAmal` را Open کنید
3. SDK Platform 35 و Build-Tools 35.x را نصب کنید
4. **Build → Build Bundle(s) / APK(s) → Build APK(s)**

---

## امکانات برنامه

- صفحه شروع و معرفی ۵ مرحله‌ای
- خانه: آیه روز، تمرین، مسیر فعال، چالش، آمار
- قرآن: فهرست ۱۱۴ سوره + آیات منتخب با محتوای تدبری
- جزئیات آیه: ترجمه (۳ مترجم)، تفسیر، واژگان، کاربرد، تمرین
- ۸ مسیر، ۱۲ چالش، ۱۲ موقعیت زندگی، ۱۰ سناریو
- آزمون و بازی آموزشی
- دفتر تدبر، گزارش پیشرفت، تنظیمات، حریم خصوصی
- ۷ تم رنگی + حالت شب
- کاملاً آفلاین

## ساختار بسته جشنواره

```
AyehTaAmal-Festival/
├── .github/workflows/   ← ساخت خودکار APK
├── Source/AyehTaAmal/   ← کد منبع اندروید
├── APK/                 ← APK (پس از CI)
├── Documents/           ← مستندات PDF
├── Images/              ← لوگو و اسکرین‌شات
└── Video/               ← ویدیو دمو
```

## فناوری

Kotlin · Jetpack Compose · MVVM · Room · DataStore · Hilt · Navigation Compose

---

**شعار:** از تلاوت تا تغییر رفتار
