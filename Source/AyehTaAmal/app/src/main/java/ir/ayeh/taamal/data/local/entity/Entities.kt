package ir.ayeh.taamal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "surahs")
data class SurahEntity(
    @PrimaryKey val number: Int,
    val nameFa: String,
    val nameAr: String,
    val ayahCount: Int,
    val revelationType: String,
    val juzStart: Int
)

@Entity(tableName = "ayahs")
data class AyahEntity(
    @PrimaryKey val id: Long,
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val pageNumber: Int,
    val juzNumber: Int,
    val topic: String,
    val keywords: String
)

@Entity(tableName = "translators")
data class TranslatorEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    val source: String,
    val isDefault: Boolean
)

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey val id: Long,
    val ayahId: Long,
    val translatorId: Long,
    val translationText: String
)

@Entity(tableName = "ayah_contents")
data class AyahContentEntity(
    @PrimaryKey val ayahId: Long,
    val message: String,
    val tafsir: String,
    val tafsirSource: String,
    val applicationsJson: String,
    val vocabularyJson: String,
    val relatedAyahIds: String
)

@Entity(tableName = "practices")
data class PracticeEntity(
    @PrimaryKey val id: Long,
    val ayahId: Long,
    val title: String,
    val description: String,
    val difficulty: String,
    val durationMinutes: Int,
    val category: String
)

@Entity(tableName = "paths")
data class PathEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val audience: String,
    val imageKey: String,
    val durationDays: Int,
    val level: String,
    val progress: Float
)

@Entity(tableName = "path_steps")
data class PathStepEntity(
    @PrimaryKey val id: Long,
    val pathId: Long,
    val orderIndex: Int,
    val ayahId: Long,
    val title: String,
    val sampleStory: String,
    val practiceText: String,
    val reflectionQuestion: String
)

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val ayahId: Long,
    val durationDays: Int,
    val instructions: String,
    val imageKey: String
)

@Entity(tableName = "quiz_questions")
data class QuizEntity(
    @PrimaryKey val id: Long,
    val question: String,
    val optionsJson: String,
    val correctIndex: Int,
    val explanation: String,
    val source: String,
    val difficulty: String,
    val topic: String
)

@Entity(tableName = "journal_notes")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ayahId: Long?,
    val title: String,
    val content: String,
    val decision: String,
    val result: String,
    val tags: String,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "scenarios")
data class ScenarioEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val situation: String,
    val optionsJson: String,
    val outcomesJson: String,
    val relatedAyahId: Long,
    val analysis: String
)

@Entity(tableName = "life_situations")
data class LifeSituationEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val ayahId: Long,
    val shortExplain: String,
    val immediateAction: String,
    val dailyPractice: String
)

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val key: String,
    val intValue: Int = 0,
    val floatValue: Float = 0f,
    val textValue: String = ""
)
