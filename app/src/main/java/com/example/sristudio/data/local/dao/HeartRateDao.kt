package com.example.sristudio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sristudio.data.local.entity.HeartRateRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface HeartRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: HeartRateRecord)

    @Query("SELECT * FROM heart_rate_records WHERE date = :date ORDER BY timestamp DESC")
    fun getByDate(date: String): Flow<List<HeartRateRecord>>

    @Query("SELECT AVG(bpm) FROM heart_rate_records WHERE date = :date")
    fun getAverageForDate(date: String): Flow<Double?>

    @Query("SELECT * FROM heart_rate_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int = 20): Flow<List<HeartRateRecord>>
}
