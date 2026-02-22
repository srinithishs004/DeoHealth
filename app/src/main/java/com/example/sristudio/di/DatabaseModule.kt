package com.example.sristudio.di

import android.content.Context
import androidx.room.Room
import com.example.sristudio.data.local.AppDatabase
import com.example.sristudio.data.local.PreferencesDataStore
import com.example.sristudio.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "deo_health_db"
        ).build()
    }

    @Provides fun provideStepDao(db: AppDatabase): StepDao = db.stepDao()
    @Provides fun provideSleepDao(db: AppDatabase): SleepDao = db.sleepDao()
    @Provides fun provideWaterDao(db: AppDatabase): WaterDao = db.waterDao()
    @Provides fun provideCalorieDao(db: AppDatabase): CalorieDao = db.calorieDao()
    @Provides fun provideWorkoutDao(db: AppDatabase): WorkoutDao = db.workoutDao()
    @Provides fun provideHeartRateDao(db: AppDatabase): HeartRateDao = db.heartRateDao()
    @Provides fun provideWellnessScoreDao(db: AppDatabase): WellnessScoreDao = db.wellnessScoreDao()
    @Provides fun provideUserAccountDao(db: AppDatabase): UserAccountDao = db.userAccountDao()

    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): PreferencesDataStore {
        return PreferencesDataStore(context)
    }
}
