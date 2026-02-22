package com.example.sristudio.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sristudio.data.local.entity.WaterRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: WaterRecord)

    @Query("SELECT * FROM water_records WHERE date = :date ORDER BY timestamp DESC")
    fun getByDate(date: String): Flow<List<WaterRecord>>

    @Query("SELECT SUM(amountMl) FROM water_records WHERE date = :date")
    fun getTotalForDate(date: String): Flow<Int?>

    @Query("SELECT * FROM water_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int = 20): Flow<List<WaterRecord>>

    @Query("SELECT date, SUM(amountMl) as totalMl FROM water_records WHERE date BETWEEN :startDate AND :endDate GROUP BY date ORDER BY date ASC")
    fun getDailyTotals(startDate: String, endDate: String): Flow<List<DailyWaterTotal>>

    @Query("DELETE FROM water_records WHERE id = :id")
    suspend fun delete(id: Long)
}

data class DailyWaterTotal(
    val date: String,
    val totalMl: Int
)
