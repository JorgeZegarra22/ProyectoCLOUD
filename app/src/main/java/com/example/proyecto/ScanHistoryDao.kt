package com.example.proyecto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanHistoryDao {

    @Insert
    suspend fun insertScan(history: ScanHistory)

    @Query("SELECT * FROM scan_history ORDER BY date DESC")
    suspend fun getAllScans(): List<ScanHistory>
}