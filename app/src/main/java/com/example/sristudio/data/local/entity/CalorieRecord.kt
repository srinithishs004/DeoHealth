package com.example.sristudio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calorie_records")
data class CalorieRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val mealName: String,
    val calories: Int,
    val mealType: String = "Snack", // Breakfast, Lunch, Dinner, Snack
    val protein: Double = 0.0, // grams
    val carbs: Double = 0.0, // grams
    val fat: Double = 0.0, // grams
    val timestamp: Long = System.currentTimeMillis()
)
