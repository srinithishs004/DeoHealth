package com.example.sristudio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wellness_scores")
data class WellnessScoreRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val score: Int, // 0-100
    val stepsScore: Int = 0,
    val sleepScore: Int = 0,
    val waterScore: Int = 0,
    val caloriesScore: Int = 0,
    val workoutScore: Int = 0,
    val tip: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
