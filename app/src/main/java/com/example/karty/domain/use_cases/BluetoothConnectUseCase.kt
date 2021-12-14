package com.example.karty.domain.use_cases

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.*
import javax.inject.Inject

private val BT_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")


class BluetoothConnectUseCase @Inject constructor(private val bluetoothAdapter: BluetoothAdapter) {
    operator fun invoke(deviceAddress: String): BluetoothSocket? {

        return try {
            val device: BluetoothDevice =
                bluetoothAdapter.getRemoteDevice(deviceAddress)
            val bluetoothSocket = device.createRfcommSocketToServiceRecord(BT_UUID)
            bluetoothAdapter.cancelDiscovery()
            bluetoothSocket!!.connect()
            //return
            bluetoothSocket
        } catch (e: IOException) {
            e.printStackTrace()
            //return
            throw IOException("Did not connect...")
        }

    }
}