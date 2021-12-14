package com.example.karty.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.karty.domain.model.RC

@Database(entities = [RC::class], version = 1 )
abstract class RcDatabase:RoomDatabase() {
    abstract fun RcDao():RcDao

}