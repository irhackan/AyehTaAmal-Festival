package ir.ayeh.taamal.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.ayeh.taamal.domain.model.UserSettings
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("ayeh_settings")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val colorScheme = stringPreferencesKey("color_scheme")
        val nightMode = stringPreferencesKey("night_mode")
        val translatorId = longPreferencesKey("translator_id")
        val reciterId = longPreferencesKey("reciter_id")
        val arabicFontScale = floatPreferencesKey("arabic_font_scale")
        val translationFontScale = floatPreferencesKey("translation_font_scale")
        val lineSpacing = floatPreferencesKey("line_spacing")
        val notificationTime = stringPreferencesKey("notification_time")
        val audienceType = stringPreferencesKey("audience_type")
        val userName = stringPreferencesKey("user_name")
        val onboardingDone = booleanPreferencesKey("onboarding_done")
        val streakDays = intPreferencesKey("streak_days")
        val totalPoints = intPreferencesKey("total_points")
        val theme = stringPreferencesKey("theme")
    }

    val settings: Flow<UserSettings> = context.dataStore.data.map { p ->
        UserSettings(
            theme = p[Keys.theme] ?: "system",
            colorScheme = p[Keys.colorScheme] ?: "quranic_green",
            nightMode = p[Keys.nightMode] ?: "system",
            translatorId = p[Keys.translatorId] ?: 1L,
            reciterId = p[Keys.reciterId] ?: 1L,
            arabicFontScale = p[Keys.arabicFontScale] ?: 1.2f,
            translationFontScale = p[Keys.translationFontScale] ?: 1f,
            lineSpacing = p[Keys.lineSpacing] ?: 1.4f,
            notificationTime = p[Keys.notificationTime] ?: "07:00",
            audienceType = p[Keys.audienceType] ?: "general",
            userName = p[Keys.userName] ?: "مهمان",
            onboardingDone = p[Keys.onboardingDone] ?: false,
            streakDays = p[Keys.streakDays] ?: 1,
            totalPoints = p[Keys.totalPoints] ?: 0
        )
    }

    suspend fun update(transform: (UserSettings) -> UserSettings) {
        context.dataStore.edit { prefs ->
            val current = UserSettings(
                theme = prefs[Keys.theme] ?: "system",
                colorScheme = prefs[Keys.colorScheme] ?: "quranic_green",
                nightMode = prefs[Keys.nightMode] ?: "system",
                translatorId = prefs[Keys.translatorId] ?: 1L,
                reciterId = prefs[Keys.reciterId] ?: 1L,
                arabicFontScale = prefs[Keys.arabicFontScale] ?: 1.2f,
                translationFontScale = prefs[Keys.translationFontScale] ?: 1f,
                lineSpacing = prefs[Keys.lineSpacing] ?: 1.4f,
                notificationTime = prefs[Keys.notificationTime] ?: "07:00",
                audienceType = prefs[Keys.audienceType] ?: "general",
                userName = prefs[Keys.userName] ?: "مهمان",
                onboardingDone = prefs[Keys.onboardingDone] ?: false,
                streakDays = prefs[Keys.streakDays] ?: 1,
                totalPoints = prefs[Keys.totalPoints] ?: 0
            )
            val next = transform(current)
            prefs[Keys.theme] = next.theme
            prefs[Keys.colorScheme] = next.colorScheme
            prefs[Keys.nightMode] = next.nightMode
            prefs[Keys.translatorId] = next.translatorId
            prefs[Keys.reciterId] = next.reciterId
            prefs[Keys.arabicFontScale] = next.arabicFontScale
            prefs[Keys.translationFontScale] = next.translationFontScale
            prefs[Keys.lineSpacing] = next.lineSpacing
            prefs[Keys.notificationTime] = next.notificationTime
            prefs[Keys.audienceType] = next.audienceType
            prefs[Keys.userName] = next.userName
            prefs[Keys.onboardingDone] = next.onboardingDone
            prefs[Keys.streakDays] = next.streakDays
            prefs[Keys.totalPoints] = next.totalPoints
        }
    }

    suspend fun addPoints(points: Int) {
        update { it.copy(totalPoints = it.totalPoints + points) }
    }
}
