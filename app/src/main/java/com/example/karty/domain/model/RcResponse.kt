package com.example.karty.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "readings_table")
data class RcResponse(
    @PrimaryKey(autoGenerate = false)
    val deviceAddress:String,
    val time: Long = System.currentTimeMillis(),
    val dateTime:String = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault()).format(Date()),
    val motorRight:Int,
    val motorLeft: Int
)
