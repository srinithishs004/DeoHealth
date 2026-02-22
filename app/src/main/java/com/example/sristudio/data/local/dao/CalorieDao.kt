package com.example.sristudio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sristudio.data.local.entity.CalorieRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface CalorieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: CalorieRecord)

    @Query("SELECT * FROM calorie_records WHERE date = :date ORDER BY timestamp DESC")
    fun getByDate(date: String): Flow<List<CalorieRecord>>

    @Query("SELECT SUM(calories) FROM calorie_records WHERE date = :date")
    fun getTotalForDate(date: String): Flow<Int?>

    @Query("SELECT * FROM calorie_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int = 20): Flow<List<CalorieRecord>>

    @Query("DELETE FROM calorie_records WHERE id = :id")
    suspend fun delete(id: Long)
}
