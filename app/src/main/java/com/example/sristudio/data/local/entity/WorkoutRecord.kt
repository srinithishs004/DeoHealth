package com.example.sristudio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_records")
data class WorkoutRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val workoutType: String, // Cardio, Strength, Yoga, Walking, Running, Cycling
    val durationMinutes: Int,
    val caloriesBurned: Int = 0,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
