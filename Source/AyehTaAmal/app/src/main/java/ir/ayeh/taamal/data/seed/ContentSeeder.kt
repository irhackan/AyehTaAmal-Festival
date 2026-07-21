package ir.ayeh.taamal.data.seed

import com.google.gson.Gson
import ir.ayeh.taamal.data.local.AppDatabase
import ir.ayeh.taamal.data.local.entity.AyahContentEntity
import ir.ayeh.taamal.data.local.entity.AyahEntity
import ir.ayeh.taamal.data.local.entity.ChallengeEntity
import ir.ayeh.taamal.data.local.entity.LifeSituationEntity
import ir.ayeh.taamal.data.local.entity.PathEntity
import ir.ayeh.taamal.data.local.entity.PathStepEntity
import ir.ayeh.taamal.data.local.entity.PracticeEntity
import ir.ayeh.taamal.data.local.entity.QuizEntity
import ir.ayeh.taamal.data.local.entity.ScenarioEntity
import ir.ayeh.taamal.data.local.entity.SurahEntity
import ir.ayeh.taamal.data.local.entity.TranslationEntity
import ir.ayeh.taamal.data.local.entity.TranslatorEntity
import ir.ayeh.taamal.data.local.entity.UserProgressEntity

/**
 * Populates the local Room database with demo content the very first time the app runs.
 * All data comes from hand-curated Kotlin objects in this package (see [SeedAyahs],
 * [SeedPractices], [SeedPaths], [SeedChallenges], [SeedQuizzes], [SeedLifeSituations]
 * and [SeedScenarios]) rather than JSON assets, so seeding never fails due to a missing
 * or malformed asset file.
 */
class ContentSeeder(private val db: AppDatabase) {

    private val gson = Gson()

    suspend fun seedIfNeeded() {
        if (db.surahDao().count() > 0) return

        seedSurahs()
        seedTranslators()
        seedAyahsTranslationsAndContents()
        seedPractices()
        seedPaths()
        seedChallenges()
        seedQuizzes()
        seedLifeSituations()
        seedScenarios()
        seedInitialProgress()
    }

    private suspend fun seedSurahs() {
        val entities = SurahCatalog.all.map { meta ->
            SurahEntity(
                number = meta.number,
                nameFa = meta.nameFa,
                nameAr = meta.nameAr,
                ayahCount = meta.ayahCount,
                revelationType = meta.type,
                juzStart = meta.juz
            )
        }
        db.surahDao().insertAll(entities)
    }

    private suspend fun seedTranslators() {
        db.translatorDao().insertAll(
            listOf(
                TranslatorEntity(
                    id = 1L,
                    name = "فولادوند",
                    description = "ترجمه استاد محمدمهدی فولادوند، دقیق و روان، مصوب دارالقرآن الکریم",
                    source = "ترجمه فولادوند",
                    isDefault = true
                ),
                TranslatorEntity(
                    id = 2L,
                    name = "مکارم شیرازی",
                    description = "ترجمه آیت‌الله ناصر مکارم شیرازی، همراه با توضیحات تفسیری کوتاه",
                    source = "ترجمه مکارم شیرازی",
                    isDefault = false
                ),
                TranslatorEntity(
                    id = 3L,
                    name = "الهی قمشه‌ای",
                    description = "ترجمه مهدی الهی قمشه‌ای، با نثر روان و ادبی",
                    source = "ترجمه الهی قمشه‌ای",
                    isDefault = false
                )
            )
        )
    }

    private suspend fun seedAyahsTranslationsAndContents() {
        val ayahEntities = mutableListOf<AyahEntity>()
        val translationEntities = mutableListOf<TranslationEntity>()
        val contentEntities = mutableListOf<AyahContentEntity>()
        var translationId = 1L

        SeedAyahs.all.forEach { seed ->
            val ayahId = seed.id

            ayahEntities += AyahEntity(
                id = ayahId,
                surahNumber = seed.surah,
                ayahNumber = seed.ayah,
                arabicText = seed.arabic,
                pageNumber = seed.page,
                juzNumber = seed.juz,
                topic = seed.topic,
                keywords = seed.keywords
            )

            translationEntities += TranslationEntity(translationId++, ayahId, 1L, seed.translations.fouladvand)
            translationEntities += TranslationEntity(translationId++, ayahId, 2L, seed.translations.makarem)
            translationEntities += TranslationEntity(translationId++, ayahId, 3L, seed.translations.elahiQomshei)

            seed.content?.let { content ->
                contentEntities += AyahContentEntity(
                    ayahId = ayahId,
                    message = content.message,
                    tafsir = content.tafsir,
                    tafsirSource = content.tafsirSource,
                    applicationsJson = gson.toJson(content.applications),
                    vocabularyJson = gson.toJson(
                        content.vocabulary.map {
                            mapOf(
                                "arabic" to it.arabic,
                                "root" to it.root,
                                "meaning" to it.meaning,
                                "note" to it.note
                            )
                        }
                    ),
                    relatedAyahIds = content.relatedAyahIds.joinToString(",")
                )
            }
        }

        db.ayahDao().insertAll(ayahEntities)
        db.translationDao().insertAll(translationEntities)
        db.ayahContentDao().insertAll(contentEntities)
    }

    private suspend fun seedPractices() {
        val entities = SeedPractices.all.mapIndexed { index, practice ->
            PracticeEntity(
                id = index + 1L,
                ayahId = ayahId(practice.surah, practice.ayah),
                title = practice.title,
                description = practice.description,
                difficulty = practice.difficulty,
                durationMinutes = practice.durationMinutes,
                category = practice.category
            )
        }
        db.practiceDao().insertAll(entities)
    }

    private suspend fun seedPaths() {
        val pathEntities = mutableListOf<PathEntity>()
        val stepEntities = mutableListOf<PathStepEntity>()
        var stepId = 1L

        SeedPaths.all.forEachIndexed { index, path ->
            val pathId = index + 1L
            pathEntities += PathEntity(
                id = pathId,
                title = path.title,
                description = path.description,
                audience = path.audience,
                imageKey = path.imageKey,
                durationDays = path.durationDays,
                level = path.level,
                progress = 0f
            )
            path.steps.forEach { step ->
                stepEntities += PathStepEntity(
                    id = stepId++,
                    pathId = pathId,
                    orderIndex = step.orderIndex,
                    ayahId = ayahId(step.surah, step.ayah),
                    title = step.title,
                    sampleStory = step.sampleStory,
                    practiceText = step.practiceText,
                    reflectionQuestion = step.reflectionQuestion
                )
            }
        }

        db.pathDao().insertPaths(pathEntities)
        db.pathDao().insertSteps(stepEntities)
    }

    private suspend fun seedChallenges() {
        val entities = SeedChallenges.all.mapIndexed { index, challenge ->
            ChallengeEntity(
                id = index + 1L,
                title = challenge.title,
                ayahId = ayahId(challenge.surah, challenge.ayah),
                durationDays = challenge.durationDays,
                instructions = challenge.instructions,
                imageKey = challenge.imageKey
            )
        }
        db.challengeDao().insertAll(entities)
    }

    private suspend fun seedQuizzes() {
        val entities = SeedQuizzes.all.mapIndexed { index, quiz ->
            QuizEntity(
                id = index + 1L,
                question = quiz.question,
                optionsJson = gson.toJson(quiz.options),
                correctIndex = quiz.correctIndex,
                explanation = quiz.explanation,
                source = quiz.source,
                difficulty = quiz.difficulty,
                topic = quiz.topic
            )
        }
        db.quizDao().insertAll(entities)
    }

    private suspend fun seedLifeSituations() {
        val entities = SeedLifeSituations.all.mapIndexed { index, situation ->
            LifeSituationEntity(
                id = index + 1L,
                title = situation.title,
                ayahId = ayahId(situation.surah, situation.ayah),
                shortExplain = situation.shortExplain,
                immediateAction = situation.immediateAction,
                dailyPractice = situation.dailyPractice
            )
        }
        db.lifeSituationDao().insertAll(entities)
    }

    private suspend fun seedScenarios() {
        val entities = SeedScenarios.all.mapIndexed { index, scenario ->
            ScenarioEntity(
                id = index + 1L,
                title = scenario.title,
                situation = scenario.situation,
                optionsJson = gson.toJson(scenario.options),
                outcomesJson = gson.toJson(scenario.outcomes),
                relatedAyahId = ayahId(scenario.surah, scenario.ayah),
                analysis = scenario.analysis
            )
        }
        db.scenarioDao().insertAll(entities)
    }

    private suspend fun seedInitialProgress() {
        listOf("ayahs_read", "practices_done", "quizzes_taken", "avg_score").forEach { key ->
            db.progressDao().upsert(UserProgressEntity(key = key, intValue = 0, floatValue = 0f, textValue = ""))
        }
    }
}
