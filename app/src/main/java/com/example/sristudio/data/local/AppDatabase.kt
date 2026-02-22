package com.example.sristudio.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sristudio.data.local.dao.*
import com.example.sristudio.data.local.entity.*

@Database(
    entities = [
        StepRecord::class,
        SleepRecord::class,
        WaterRecord::class,
        CalorieRecord::class,
        WorkoutRecord::class,
        HeartRateRecord::class,
        WellnessScoreRecord::class,
        UserAccount::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun stepDao(): StepDao
    abstract fun sleepDao(): SleepDao
    abstract fun waterDao(): WaterDao
    abstract fun calorieDao(): CalorieDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun heartRateDao(): HeartRateDao
    abstract fun wellnessScoreDao(): WellnessScoreDao
    abstract fun userAccountDao(): UserAccountDao
}
