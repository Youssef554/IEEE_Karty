package com.example.karty.core.di

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.example.karty.domain.use_cases.BluetoothConnectUseCase
import com.example.karty.domain.use_cases.BluetoothDisconnectUseCase
import com.example.karty.domain.use_cases.BluetoothSendCommandUseCase
import com.example.karty.domain.use_cases.BluetoothUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideBluetoothManger(context: Application) =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    @Provides
    fun provideBluetoothAdapter(bluetoothManager: BluetoothManager): BluetoothAdapter = bluetoothManager.adapter

    @Provides
    fun provideBluetoothUseCases(bluetoothAdapter: BluetoothAdapter)  = BluetoothUseCases(
        connect = BluetoothConnectUseCase(bluetoothAdapter),
        disconnect = BluetoothDisconnectUseCase(),
        sendCommand = BluetoothSendCommandUseCase(),
    )

}