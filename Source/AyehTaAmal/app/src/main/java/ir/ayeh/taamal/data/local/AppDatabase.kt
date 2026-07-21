package ir.ayeh.taamal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.ayeh.taamal.data.local.dao.AyahContentDao
import ir.ayeh.taamal.data.local.dao.AyahDao
import ir.ayeh.taamal.data.local.dao.ChallengeDao
import ir.ayeh.taamal.data.local.dao.JournalDao
import ir.ayeh.taamal.data.local.dao.LifeSituationDao
import ir.ayeh.taamal.data.local.dao.PathDao
import ir.ayeh.taamal.data.local.dao.PracticeDao
import ir.ayeh.taamal.data.local.dao.ProgressDao
import ir.ayeh.taamal.data.local.dao.QuizDao
import ir.ayeh.taamal.data.local.dao.ScenarioDao
import ir.ayeh.taamal.data.local.dao.SurahDao
import ir.ayeh.taamal.data.local.dao.TranslationDao
import ir.ayeh.taamal.data.local.dao.TranslatorDao
import ir.ayeh.taamal.data.local.entity.AyahContentEntity
import ir.ayeh.taamal.data.local.entity.AyahEntity
import ir.ayeh.taamal.data.local.entity.ChallengeEntity
import ir.ayeh.taamal.data.local.entity.JournalEntity
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

@Database(
    entities = [
        SurahEntity::class,
        AyahEntity::class,
        TranslatorEntity::class,
        TranslationEntity::class,
        AyahContentEntity::class,
        PracticeEntity::class,
        PathEntity::class,
        PathStepEntity::class,
        ChallengeEntity::class,
        QuizEntity::class,
        JournalEntity::class,
        ScenarioEntity::class,
        LifeSituationEntity::class,
        UserProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun surahDao(): SurahDao
    abstract fun ayahDao(): AyahDao
    abstract fun translationDao(): TranslationDao
    abstract fun translatorDao(): TranslatorDao
    abstract fun ayahContentDao(): AyahContentDao
    abstract fun practiceDao(): PracticeDao
    abstract fun pathDao(): PathDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun quizDao(): QuizDao
    abstract fun journalDao(): JournalDao
    abstract fun scenarioDao(): ScenarioDao
    abstract fun lifeSituationDao(): LifeSituationDao
    abstract fun progressDao(): ProgressDao
}
