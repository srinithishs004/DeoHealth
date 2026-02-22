package com.example.sristudio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_records")
data class StepRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val stepCount: Int,
    val goalSteps: Int = 8000,
    val caloriesBurned: Double = 0.0,
    val distanceKm: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
