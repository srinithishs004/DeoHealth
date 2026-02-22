package com.example.sristudio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_records")
data class SleepRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val sleepDurationMinutes: Int, // total sleep in minutes
    val bedTime: String, // HH:mm
    val wakeTime: String, // HH:mm
    val quality: String = "Unknown", // Good, Fair, Poor, Unknown
    val goalMinutes: Int = 480, // 8 hours default
    val timestamp: Long = System.currentTimeMillis()
)
