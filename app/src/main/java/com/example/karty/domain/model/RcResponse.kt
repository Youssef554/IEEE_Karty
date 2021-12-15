package com.example.karty.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readings_table")
data class RcResponse(
    @PrimaryKey(autoGenerate = false)
    val deviceAddress:String,
    val time: Long = System.currentTimeMillis(),
    val motorRight:Int,
    val motorLeft: Int
)
