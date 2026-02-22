package com.example.sristudio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heart_rate_records")
data class HeartRateRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val bpm: Int,
    val source: String = "Manual", // Manual, GoogleFit, WearOS
    val timestamp: Long = System.currentTimeMillis()
)
