package com.example.karty.core.di

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.karty.data.data_source.RcDatabase
import com.example.karty.domain.use_cases.bluetooth.BluetoothConnectUseCase
import com.example.karty.domain.use_cases.bluetooth.BluetoothDisconnectUseCase
import com.example.karty.domain.use_cases.bluetooth.BluetoothSendCommandUseCase
import com.example.karty.domain.use_cases.bluetooth.BluetoothUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Application) =
        Room.databaseBuilder(context, RoomDatabase::class.java, "rc_database")
            .fallbackToDestructiveMigration().build()

    @Provides
    fun provideDao(database: RcDatabase) = database.RcDao()

    @Provides
    fun provideBluetoothManger(context: Application) =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    @Provides
    fun provideBluetoothAdapter(bluetoothManager: BluetoothManager): BluetoothAdapter =
        bluetoothManager.adapter

    @Provides
    fun provideBluetoothUseCases(bluetoothAdapter: BluetoothAdapter) = BluetoothUseCases(
        connect = BluetoothConnectUseCase(bluetoothAdapter),
        disconnect = BluetoothDisconnectUseCase(),
        sendCommand = BluetoothSendCommandUseCase(),
    )

}