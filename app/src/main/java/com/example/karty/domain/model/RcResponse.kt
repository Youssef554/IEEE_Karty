package com.example.karty.domain.model

import androidx.room.Entity

@Entity(tableName = "rc_readings")
data class RcResponse(
    val motorRight:Int,
    val motorLeft: Int
)
