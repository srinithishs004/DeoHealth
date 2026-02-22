package com.example.sristudio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_records")
data class WaterRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val amountMl: Int, // amount in milliliters
    val source: String = "Water", // Water, Tea, Coffee, Juice, etc.
    val timestamp: Long = System.currentTimeMillis()
)
