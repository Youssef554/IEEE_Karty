package com.example.karty.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.karty.domain.model.RC
import com.example.karty.domain.model.RcResponse

@Dao
interface RcDao {
    @Query("SELECT * from rc_table")
    suspend fun getDevices():List<RC>

    @Query("SELECT * from rc_table where deviceAddress = :address")
    suspend fun getDeviceByAddress(address:String):RC

    @Query("SELECT * from readings_table where deviceAddress = :address")
    suspend fun getDeviceReadings(address: String):List<RcResponse>

    @Insert(onConflict = REPLACE)
    suspend fun addDevice(rc: RC)

    @Query("DELETE FROM rc_table where deviceAddress = :address")
    suspend fun deleteDeviceByAddress(address: String)
}