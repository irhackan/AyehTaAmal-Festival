package ir.ayeh.taamal.domain.model

data class Ayah(
    val id: Long,
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val pageNumber: Int,
    val juzNumber: Int,
    val topic: String,
    val keywords: String
)

data class Translation(
    val id: Long,
    val ayahId: Long,
    val translatorId: Long,
    val translationText: String
)

data class Translator(
    val id: Long,
    val name: String,
    val description: String,
    val source: String,
    val isDefault: Boolean
)

data class Reciter(
    val id: Long,
    val name: String,
    val style: String
)

data class Practice(
    val id: Long,
    val ayahId: Long,
    val title: String,
    val description: String,
    val difficulty: String,
    val durationMinutes: Int,
    val category: String
)

data class PathItem(
    val id: Long,
    val title: String,
    val description: String,
    val audience: String,
    val imageKey: String,
    val durationDays: Int,
    val level: String,
    val progress: Float
)

data class PathStep(
    val id: Long,
    val pathId: Long,
    val order: Int,
    val ayahId: Long,
    val title: String,
    val sampleStory: String,
    val practiceText: String,
    val reflectionQuestion: String
)

data class ChallengeItem(
    val id: Long,
    val title: String,
    val ayahId: Long,
    val durationDays: Int,
    val instructions: String,
    val imageKey: String
)

data class QuizQuestion(
    val id: Long,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val source: String,
    val difficulty: String,
    val topic: String
)

data class JournalNote(
    val id: Long = 0,
    val ayahId: Long?,
    val title: String,
    val content: String,
    val decision: String,
    val result: String,
    val tags: String,
    val createdAt: Long,
    val updatedAt: Long
)

data class ScenarioItem(
    val id: Long,
    val title: String,
    val situation: String,
    val options: List<String>,
    val outcomes: List<String>,
    val relatedAyahId: Long,
    val analysis: String
)

data class UserSettings(
    val theme: String = "system",
    val colorScheme: String = "quranic_green",
    val nightMode: String = "system",
    val translatorId: Long = 1,
    val reciterId: Long = 1,
    val arabicFontScale: Float = 1.2f,
    val translationFontScale: Float = 1f,
    val lineSpacing: Float = 1.4f,
    val notificationTime: String = "07:00",
    val audienceType: String = "general",
    val userName: String = "مهمان",
    val onboardingDone: Boolean = false,
    val streakDays: Int = 1,
    val totalPoints: Int = 0
)

data class WeeklyStats(
    val ayahsRead: Int = 0,
    val practicesDone: Int = 0,
    val quizzesTaken: Int = 0,
    val avgScore: Int = 0,
    val notesCount: Int = 0,
    val streak: Int = 0
)

data class SurahInfo(
    val number: Int,
    val nameFa: String,
    val nameAr: String,
    val ayahCount: Int,
    val revelationType: String,
    val juzStart: Int
)

data class AyahDetailContent(
    val ayah: Ayah,
    val translations: List<Translation>,
    val message: String,
    val tafsir: String,
    val tafsirSource: String,
    val applications: Map<String, String>,
    val practices: List<Practice>,
    val vocabulary: List<VocabularyItem>,
    val relatedAyahIds: List<Long>
)

data class VocabularyItem(
    val arabic: String,
    val root: String,
    val meaning: String,
    val note: String
)

data class LifeSituation(
    val id: Long,
    val title: String,
    val ayahId: Long,
    val shortExplain: String,
    val immediateAction: String,
    val dailyPractice: String
)

data class HomeSnapshot(
    val ayah: Ayah?,
    val translation: String,
    val message: String,
    val practice: Practice?,
    val path: PathItem?,
    val challenge: ChallengeItem?,
    val stats: WeeklyStats
)
