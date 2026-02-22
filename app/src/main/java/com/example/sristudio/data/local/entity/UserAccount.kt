package com.example.sristudio.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String = "",
    val phone: String = "",
    val passwordHash: String,
    val displayName: String,
    val age: Int = 0,
    val weightKg: Double = 0.0,
    val heightCm: Double = 0.0,
    val gender: String = "Not specified", // Male, Female, Not specified
    val createdAt: Long = System.currentTimeMillis()
)
