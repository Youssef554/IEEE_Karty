package com.example.karty.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rc_table")
data class RC(
    @PrimaryKey(autoGenerate = false)
    val deviceAddress: String,
    val deviceName: String,

    )
