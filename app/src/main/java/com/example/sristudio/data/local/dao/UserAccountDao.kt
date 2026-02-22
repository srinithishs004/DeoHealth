package com.example.sristudio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sristudio.data.local.entity.UserAccount

@Dao
interface UserAccountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(account: UserAccount): Long

    @Query("SELECT * FROM user_accounts WHERE (email = :emailOrPhone OR phone = :emailOrPhone) AND passwordHash = :passwordHash LIMIT 1")
    suspend fun authenticate(emailOrPhone: String, passwordHash: String): UserAccount?

    @Query("SELECT EXISTS(SELECT 1 FROM user_accounts WHERE email = :email OR phone = :email)")
    suspend fun isRegistered(email: String): Boolean

    @Query("SELECT * FROM user_accounts WHERE id = :userId LIMIT 1")
    suspend fun getById(userId: Long): UserAccount?

    @Query("SELECT * FROM user_accounts WHERE id = :userId LIMIT 1")
    fun observeById(userId: Long): kotlinx.coroutines.flow.Flow<UserAccount?>

    @Query("UPDATE user_accounts SET displayName = :name, age = :age, weightKg = :weight, heightCm = :height, gender = :gender WHERE id = :userId")
    suspend fun updateProfile(userId: Long, name: String, age: Int, weight: Double, height: Double, gender: String)
}
