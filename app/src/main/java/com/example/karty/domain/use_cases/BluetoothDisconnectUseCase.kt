package com.example.karty.domain.use_cases

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException

class BluetoothDisconnectUseCase {
    operator fun invoke(socket: BluetoothSocket){
        try {
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
            throw IOException("Error Disconnecting.....")
        }
    }
}