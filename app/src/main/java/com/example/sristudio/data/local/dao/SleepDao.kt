package com.example.sristudio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sristudio.data.local.entity.SleepRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: SleepRecord)

    @Query("SELECT * FROM sleep_records WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): SleepRecord?

    @Query("SELECT * FROM sleep_records WHERE date = :date LIMIT 1")
    fun observeByDate(date: String): Flow<SleepRecord?>

    @Query("SELECT * FROM sleep_records ORDER BY date DESC LIMIT :limit")
    fun getRecent(limit: Int = 7): Flow<List<SleepRecord>>

    @Query("SELECT * FROM sleep_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getRange(startDate: String, endDate: String): Flow<List<SleepRecord>>
}
