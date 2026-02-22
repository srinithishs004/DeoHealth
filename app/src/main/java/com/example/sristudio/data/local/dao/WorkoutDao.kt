package com.example.sristudio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sristudio.data.local.entity.WorkoutRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: WorkoutRecord)

    @Query("SELECT * FROM workout_records WHERE date = :date ORDER BY timestamp DESC")
    fun getByDate(date: String): Flow<List<WorkoutRecord>>

    @Query("SELECT SUM(durationMinutes) FROM workout_records WHERE date = :date")
    fun getTotalMinutesForDate(date: String): Flow<Int?>

    @Query("SELECT * FROM workout_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int = 20): Flow<List<WorkoutRecord>>

    @Query("DELETE FROM workout_records WHERE id = :id")
    suspend fun delete(id: Long)
}
