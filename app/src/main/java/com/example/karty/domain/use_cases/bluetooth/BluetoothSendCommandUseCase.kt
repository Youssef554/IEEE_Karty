package com.example.karty.domain.use_cases.bluetooth

import android.bluetooth.BluetoothSocket
import java.io.IOException

class BluetoothSendCommandUseCase {
    operator fun invoke(socket: BluetoothSocket,command:String){
        try {
            socket.outputStream.write(command.toByteArray())
        } catch (e: IOException) {
            throw IOException("Could not send command")
        }
    }
}