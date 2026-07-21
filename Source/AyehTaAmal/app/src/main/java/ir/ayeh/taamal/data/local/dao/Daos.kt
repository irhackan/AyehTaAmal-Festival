package ir.ayeh.taamal.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface SurahDao {
    @Query("SELECT * FROM surahs ORDER BY number")
    fun observeAll(): Flow<List<SurahEntity>>

    @Query("SELECT * FROM surahs ORDER BY number")
    suspend fun getAll(): List<SurahEntity>

    @Query("SELECT COUNT(*) FROM surahs")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<SurahEntity>)
}

@Dao
interface AyahDao {
    @Query("SELECT * FROM ayahs WHERE surahNumber = :surah ORDER BY ayahNumber")
    fun observeBySurah(surah: Int): Flow<List<AyahEntity>>

    @Query("SELECT * FROM ayahs WHERE surahNumber = :surah ORDER BY ayahNumber")
    suspend fun getBySurah(surah: Int): List<AyahEntity>

    @Query("SELECT * FROM ayahs WHERE id = :id")
    suspend fun getById(id: Long): AyahEntity?

    @Query(
        """
        SELECT * FROM ayahs
        WHERE arabicText LIKE '%' || :q || '%'
           OR topic LIKE '%' || :q || '%'
           OR keywords LIKE '%' || :q || '%'
        LIMIT 50
        """
    )
    suspend fun search(q: String): List<AyahEntity>

    @Query("SELECT * FROM ayahs ORDER BY id LIMIT 1 OFFSET :offset")
    suspend fun getByOffset(offset: Int): AyahEntity?

    @Query("SELECT COUNT(*) FROM ayahs")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<AyahEntity>)
}

@Dao
interface TranslationDao {
    @Query("SELECT * FROM translations WHERE ayahId = :ayahId")
    suspend fun getForAyah(ayahId: Long): List<TranslationEntity>

    @Query("SELECT * FROM translations WHERE ayahId = :ayahId AND translatorId = :translatorId LIMIT 1")
    suspend fun getOne(ayahId: Long, translatorId: Long): TranslationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TranslationEntity>)
}

@Dao
interface TranslatorDao {
    @Query("SELECT * FROM translators ORDER BY id")
    fun observeAll(): Flow<List<TranslatorEntity>>

    @Query("SELECT * FROM translators ORDER BY id")
    suspend fun getAll(): List<TranslatorEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TranslatorEntity>)
}

@Dao
interface AyahContentDao {
    @Query("SELECT * FROM ayah_contents WHERE ayahId = :ayahId")
    suspend fun get(ayahId: Long): AyahContentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<AyahContentEntity>)
}

@Dao
interface PracticeDao {
    @Query("SELECT * FROM practices ORDER BY id")
    fun observeAll(): Flow<List<PracticeEntity>>

    @Query("SELECT * FROM practices WHERE ayahId = :ayahId")
    suspend fun getForAyah(ayahId: Long): List<PracticeEntity>

    @Query("SELECT * FROM practices ORDER BY id LIMIT 1 OFFSET :offset")
    suspend fun getByOffset(offset: Int): PracticeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<PracticeEntity>)
}

@Dao
interface PathDao {
    @Query("SELECT * FROM paths ORDER BY id")
    fun observeAll(): Flow<List<PathEntity>>

    @Query("SELECT * FROM paths WHERE id = :id")
    suspend fun get(id: Long): PathEntity?

    @Query("SELECT * FROM path_steps WHERE pathId = :pathId ORDER BY orderIndex")
    suspend fun getSteps(pathId: Long): List<PathStepEntity>

    @Update
    suspend fun update(path: PathEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaths(items: List<PathEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(items: List<PathStepEntity>)
}

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges ORDER BY id")
    fun observeAll(): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE id = :id")
    suspend fun get(id: Long): ChallengeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ChallengeEntity>)
}

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_questions ORDER BY id")
    fun observeAll(): Flow<List<QuizEntity>>

    @Query("SELECT * FROM quiz_questions ORDER BY RANDOM() LIMIT :limit")
    suspend fun random(limit: Int): List<QuizEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<QuizEntity>)
}

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_notes ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<JournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(note: JournalEntity): Long

    @Query("DELETE FROM journal_notes WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT COUNT(*) FROM journal_notes")
    suspend fun count(): Int
}

@Dao
interface ScenarioDao {
    @Query("SELECT * FROM scenarios ORDER BY id")
    fun observeAll(): Flow<List<ScenarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ScenarioEntity>)
}

@Dao
interface LifeSituationDao {
    @Query("SELECT * FROM life_situations ORDER BY id")
    fun observeAll(): Flow<List<LifeSituationEntity>>

    @Query("SELECT * FROM life_situations WHERE id = :id")
    suspend fun get(id: Long): LifeSituationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LifeSituationEntity>)
}

@Dao
interface ProgressDao {
    @Query("SELECT * FROM user_progress WHERE `key` = :key")
    suspend fun get(key: String): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: UserProgressEntity)
}
