package com.example.karty.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.karty.domain.model.RC
import com.example.karty.domain.model.RcResponse

@Dao
interface RcDao {
    @Query("SELECT * from rc_table ORDER BY deviceName DESC")
    suspend fun getDevices():List<RC>

    @Query("SELECT * from rc_table where deviceAddress = :address")
    suspend fun getDeviceByAddress(address:String):RC

    @Query("SELECT * from readings_table where deviceAddress = :address ORDER BY id DESC")
    suspend fun getDeviceReadings(address: String):List<RcResponse>

    @Insert(entity = RcResponse::class, onConflict = IGNORE)
    suspend fun addReading(reading: RcResponse)

    @Insert(entity = RC::class,onConflict = REPLACE)
    suspend fun addDevice(rc: RC)

    @Query("DELETE FROM rc_table where deviceAddress = :address")
    suspend fun deleteDeviceByAddress(address: String)
}