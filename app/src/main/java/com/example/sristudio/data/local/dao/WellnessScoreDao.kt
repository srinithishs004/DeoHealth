package com.example.sristudio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sristudio.data.local.entity.WellnessScoreRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface WellnessScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: WellnessScoreRecord)

    @Query("SELECT * FROM wellness_scores WHERE date = :date LIMIT 1")
    fun observeByDate(date: String): Flow<WellnessScoreRecord?>

    @Query("SELECT * FROM wellness_scores ORDER BY date DESC LIMIT :limit")
    fun getRecent(limit: Int = 7): Flow<List<WellnessScoreRecord>>
}
