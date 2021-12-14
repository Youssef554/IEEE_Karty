package com.example.karty.data.data_source

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.karty.domain.model.RC
import com.example.karty.domain.model.RcResponse

interface RcDao {
    @Query("SELECT * from rc_table")
    suspend fun getDevices():List<RC>

    @Query("SELECT * from rc_table where deviceAddress = :address")
    suspend fun getDeviceByAddress(address:String):RC

    @Query("SELECT readings from rc_table where deviceAddress = :address")
    suspend fun getDeviceReadings(address: String):List<RcResponse>

    @Insert()
    suspend fun addDevice(rc: RC)

    @Query("DELETE FROM rc_table where deviceAddress = :address")
    suspend fun deleteDeviceByAddress(address: String)
}