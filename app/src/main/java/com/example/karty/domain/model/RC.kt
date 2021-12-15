package com.example.karty.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rc_table")
data class RC(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val deviceName:String,
    val deviceAddress:String,
)
