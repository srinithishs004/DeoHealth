package com.example.sristudio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sristudio.data.local.entity.StepRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: StepRecord)

    @Query("SELECT * FROM step_records WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): StepRecord?

    @Query("SELECT * FROM step_records WHERE date = :date LIMIT 1")
    fun observeByDate(date: String): Flow<StepRecord?>

    @Query("SELECT * FROM step_records ORDER BY date DESC LIMIT :limit")
    fun getRecent(limit: Int = 7): Flow<List<StepRecord>>

    @Query("SELECT SUM(stepCount) FROM step_records WHERE date = :date")
    suspend fun getTotalStepsForDate(date: String): Int?

    @Query("SELECT * FROM step_records WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun getRange(startDate: String, endDate: String): Flow<List<StepRecord>>
}
