package ir.ayeh.taamal.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.ayeh.taamal.data.local.AppDatabase
import ir.ayeh.taamal.data.preferences.UserPreferencesRepository
import ir.ayeh.taamal.data.repository.ContentRepository
import ir.ayeh.taamal.data.seed.ContentSeeder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "ayeh_ta_amal.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideSeeder(db: AppDatabase): ContentSeeder = ContentSeeder(db)

    @Provides
    @Singleton
    fun provideContentRepository(
        db: AppDatabase,
        prefs: UserPreferencesRepository
    ): ContentRepository = ContentRepository(db, prefs)
}
