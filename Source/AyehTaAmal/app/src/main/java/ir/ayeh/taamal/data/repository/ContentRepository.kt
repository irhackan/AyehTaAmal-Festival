package ir.ayeh.taamal.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.ayeh.taamal.data.local.AppDatabase
import ir.ayeh.taamal.data.local.entity.JournalEntity
import ir.ayeh.taamal.data.local.entity.PathEntity
import ir.ayeh.taamal.data.local.entity.UserProgressEntity
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.domain.model.Ayah
import ir.ayeh.taamal.domain.model.AyahDetailContent
import ir.ayeh.taamal.domain.model.ChallengeItem
import ir.ayeh.taamal.domain.model.HomeSnapshot
import ir.ayeh.taamal.domain.model.JournalNote
import ir.ayeh.taamal.domain.model.LifeSituation
import ir.ayeh.taamal.domain.model.PathItem
import ir.ayeh.taamal.domain.model.PathStep
import ir.ayeh.taamal.domain.model.Practice
import ir.ayeh.taamal.domain.model.QuizQuestion
import ir.ayeh.taamal.domain.model.ScenarioItem
import ir.ayeh.taamal.domain.model.SurahInfo
import ir.ayeh.taamal.domain.model.Translation
import ir.ayeh.taamal.domain.model.Translator
import ir.ayeh.taamal.domain.model.VocabularyItem
import ir.ayeh.taamal.domain.model.WeeklyStats
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

@Singleton
class ContentRepository @Inject constructor(
    private val db: AppDatabase,
    private val prefs: UserPreferencesRepository
) {
    private val gson = Gson()

    fun observeSurahs(): Flow<List<SurahInfo>> = db.surahDao().observeAll().map { list ->
        list.map {
            SurahInfo(it.number, it.nameFa, it.nameAr, it.ayahCount, it.revelationType, it.juzStart)
        }
    }

    fun observePaths(): Flow<List<PathItem>> = db.pathDao().observeAll().map { list ->
        list.map {
            PathItem(it.id, it.title, it.description, it.audience, it.imageKey, it.durationDays, it.level, it.progress)
        }
    }

    fun observeChallenges(): Flow<List<ChallengeItem>> = db.challengeDao().observeAll().map { list ->
        list.map {
            ChallengeItem(it.id, it.title, it.ayahId, it.durationDays, it.instructions, it.imageKey)
        }
    }

    fun observeJournal(): Flow<List<JournalNote>> = db.journalDao().observeAll().map { list ->
        list.map {
            JournalNote(it.id, it.ayahId, it.title, it.content, it.decision, it.result, it.tags, it.createdAt, it.updatedAt)
        }
    }

    fun observeTranslators(): Flow<List<Translator>> = db.translatorDao().observeAll().map { list ->
        list.map { Translator(it.id, it.name, it.description, it.source, it.isDefault) }
    }

    fun observeSituations(): Flow<List<LifeSituation>> = db.lifeSituationDao().observeAll().map { list ->
        list.map {
            LifeSituation(it.id, it.title, it.ayahId, it.shortExplain, it.immediateAction, it.dailyPractice)
        }
    }

    fun observeScenarios(): Flow<List<ScenarioItem>> = db.scenarioDao().observeAll().map { list ->
        list.map {
            ScenarioItem(
                it.id, it.title, it.situation,
                decodeList(it.optionsJson), decodeList(it.outcomesJson),
                it.relatedAyahId, it.analysis
            )
        }
    }

    fun observePractices(): Flow<List<Practice>> = db.practiceDao().observeAll().map { list ->
        list.map {
            Practice(it.id, it.ayahId, it.title, it.description, it.difficulty, it.durationMinutes, it.category)
        }
    }

    suspend fun getAyahsBySurah(surah: Int): List<Ayah> =
        db.ayahDao().getBySurah(surah).map { it.toDomain() }

    suspend fun search(query: String): List<Ayah> =
        db.ayahDao().search(query.trim()).map { it.toDomain() }

    suspend fun getAyahDetail(ayahId: Long, translatorId: Long): AyahDetailContent? {
        val ayah = db.ayahDao().getById(ayahId)?.toDomain() ?: return null
        val translations = db.translationDao().getForAyah(ayahId).map {
            Translation(it.id, it.ayahId, it.translatorId, it.translationText)
        }
        val content = db.ayahContentDao().get(ayahId)
        val practices = db.practiceDao().getForAyah(ayahId).map {
            Practice(it.id, it.ayahId, it.title, it.description, it.difficulty, it.durationMinutes, it.category)
        }
        val apps: Map<String, String> = content?.applicationsJson?.let { decodeMap(it) } ?: emptyMap()
        val vocab: List<VocabularyItem> = content?.vocabularyJson?.let { decodeVocab(it) } ?: emptyList()
        val related = content?.relatedAyahIds
            ?.split(',')
            ?.mapNotNull { it.trim().toLongOrNull() }
            ?: emptyList()
        return AyahDetailContent(
            ayah = ayah,
            translations = translations.ifEmpty {
                listOf(Translation(0, ayahId, translatorId, "ترجمه در دسترس نیست"))
            },
            message = content?.message.orEmpty(),
            tafsir = content?.tafsir.orEmpty(),
            tafsirSource = content?.tafsirSource.orEmpty(),
            applications = apps,
            practices = practices,
            vocabulary = vocab,
            relatedAyahIds = related
        )
    }

    suspend fun getPathSteps(pathId: Long): List<PathStep> =
        db.pathDao().getSteps(pathId).map {
            PathStep(it.id, it.pathId, it.orderIndex, it.ayahId, it.title, it.sampleStory, it.practiceText, it.reflectionQuestion)
        }

    suspend fun updatePathProgress(pathId: Long, progress: Float) {
        val path = db.pathDao().get(pathId) ?: return
        db.pathDao().update(path.copy(progress = progress.coerceIn(0f, 1f)))
    }

    suspend fun saveJournal(note: JournalNote): Long {
        return db.journalDao().upsert(
            JournalEntity(
                id = note.id,
                ayahId = note.ayahId,
                title = note.title,
                content = note.content,
                decision = note.decision,
                result = note.result,
                tags = note.tags,
                createdAt = note.createdAt,
                updatedAt = note.updatedAt
            )
        )
    }

    suspend fun deleteJournal(id: Long) = db.journalDao().delete(id)

    suspend fun randomQuiz(limit: Int): List<QuizQuestion> =
        db.quizDao().random(limit).map {
            QuizQuestion(
                it.id, it.question, decodeList(it.optionsJson), it.correctIndex,
                it.explanation, it.source, it.difficulty, it.topic
            )
        }

    suspend fun homeSnapshot(): HomeSnapshot {
        val settings = prefs.settings.first()
        val dayIndex = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val ayahCount = db.ayahDao().count().coerceAtLeast(1)
        val ayahEntity = db.ayahDao().getByOffset(dayIndex % ayahCount)
        val ayah = ayahEntity?.toDomain()
        val translation = ayah?.let {
            db.translationDao().getOne(it.id, settings.translatorId)?.translationText
                ?: db.translationDao().getForAyah(it.id).firstOrNull()?.translationText
                ?: ""
        }.orEmpty()
        val message = ayah?.let { db.ayahContentDao().get(it.id)?.message }.orEmpty()
        val practiceCount = db.practiceDao().observeAll().map { it.size }.first().coerceAtLeast(1)
        val practiceEntity = db.practiceDao().getByOffset(dayIndex % practiceCount)
        val practice = practiceEntity?.let {
            Practice(it.id, it.ayahId, it.title, it.description, it.difficulty, it.durationMinutes, it.category)
        }
        val path = db.pathDao().observeAll().first().firstOrNull()?.let {
            PathItem(it.id, it.title, it.description, it.audience, it.imageKey, it.durationDays, it.level, it.progress)
        }
        val challenge = db.challengeDao().observeAll().first().firstOrNull()?.let {
            ChallengeItem(it.id, it.title, it.ayahId, it.durationDays, it.instructions, it.imageKey)
        }
        val notes = db.journalDao().count()
        val ayahsRead = db.progressDao().get("ayahs_read")?.intValue ?: 3
        val practicesDone = db.progressDao().get("practices_done")?.intValue ?: 1
        val quizzes = db.progressDao().get("quizzes_taken")?.intValue ?: 0
        val avg = db.progressDao().get("avg_score")?.intValue ?: 80
        return HomeSnapshot(
            ayah = ayah,
            translation = translation,
            message = message.ifBlank { "هر آیه می‌تواند امروز یک رفتار بهتر بسازد." },
            practice = practice,
            path = path,
            challenge = challenge,
            stats = WeeklyStats(ayahsRead, practicesDone, quizzes, avg, notes, settings.streakDays)
        )
    }

    suspend fun bumpProgress(key: String, delta: Int = 1) {
        val current = db.progressDao().get(key)?.intValue ?: 0
        db.progressDao().upsert(UserProgressEntity(key, intValue = current + delta))
    }

    private fun ir.ayeh.taamal.data.local.entity.AyahEntity.toDomain() =
        Ayah(id, surahNumber, ayahNumber, arabicText, pageNumber, juzNumber, topic, keywords)

    private fun decodeList(json: String): List<String> =
        runCatching {
            gson.fromJson<List<String>>(json, object : TypeToken<List<String>>() {}.type)
        }.getOrDefault(emptyList())

    private fun decodeMap(json: String): Map<String, String> =
        runCatching {
            gson.fromJson<Map<String, String>>(json, object : TypeToken<Map<String, String>>() {}.type)
        }.getOrDefault(emptyMap())

    private fun decodeVocab(json: String): List<VocabularyItem> =
        runCatching {
            gson.fromJson<List<VocabularyItem>>(json, object : TypeToken<List<VocabularyItem>>() {}.type)
        }.getOrDefault(emptyList())
}
