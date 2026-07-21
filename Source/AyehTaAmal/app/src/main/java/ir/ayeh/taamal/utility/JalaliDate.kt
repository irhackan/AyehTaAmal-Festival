package ir.ayeh.taamal.utility

import java.util.Calendar

object JalaliDate {
    private val monthNames = listOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )

    fun todayFa(): String {
        val g = Calendar.getInstance()
        val gy = g.get(Calendar.YEAR)
        val gm = g.get(Calendar.MONTH) + 1
        val gd = g.get(Calendar.DAY_OF_MONTH)
        val (jy, jm, jd) = gregorianToJalali(gy, gm, gd)
        return "$jd ${monthNames[jm - 1]} $jy"
    }

    private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): Triple<Int, Int, Int> {
        val gdm = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        var jy = if (gy <= 1600) 0 else 979
        var gy2 = if (gy <= 1600) gy - 621 else gy - 1600
        val gyMinus = gy2 - 1
        var days = (365 * gyMinus) + (gyMinus / 4) - (gyMinus / 100) + (gyMinus / 400) -
            80 + gd + gdm[gm - 1]
        if (gm > 2 && ((gy % 4 == 0 && gy % 100 != 0) || gy % 400 == 0)) days++
        jy += 33 * (days / 12053)
        days %= 12053
        jy += 4 * (days / 1461)
        days %= 1461
        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }
        val jm: Int
        val jd: Int
        if (days < 186) {
            jm = 1 + days / 31
            jd = 1 + days % 31
        } else {
            jm = 7 + (days - 186) / 30
            jd = 1 + (days - 186) % 30
        }
        return Triple(jy, jm, jd)
    }
}
