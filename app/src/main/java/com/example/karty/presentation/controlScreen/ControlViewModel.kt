package com.example.karty.presentation.controlScreen

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.io.IOException
import java.util.*

private val BT_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
private const val DELAY = 200L
lateinit var bluetoothAdapter: BluetoothAdapter


class ControlViewModel : ViewModel() {
    var bluetoothSocket: BluetoothSocket? = null

    private var _isConnected: MutableLiveData<Boolean> = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = _isConnected


    fun moveWhileBtnPressed(motionEvent: MotionEvent, position: String): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN) {
            val job = Job()
            CoroutineScope(Dispatchers.Main + job).launch {
                while (true) {
                    Log.d("ttt", "onCreate: sending command, to move in position ($position)")
                    sendCommand(position)
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        break
                    }
                    delay(DELAY)
                }
            }
        }
        return true
    }

    fun connect(context: Context, deviceAddress: String) {
        var connectionSuccess = true
        viewModelScope.executeAsyncTask(
            onPreExecute = {
                Log.d("ttt", "connect: trying to connect....")
            },
            doInBackground = {
                try {
                    if (bluetoothSocket == null || !isConnected.value!!) {
                        val bluetoothManger =
                            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                        bluetoothAdapter = bluetoothManger.adapter
                        val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)
                        bluetoothSocket = device.createRfcommSocketToServiceRecord(BT_UUID)
                        bluetoothAdapter.cancelDiscovery()
                        bluetoothSocket!!.connect()
                    }
                } catch (e: IOException) {
                    connectionSuccess = false
                    Log.e("ttt", "connect: ${e.message}")
                    e.printStackTrace()
                }
            },
            onPostExecute = {
                if (!connectionSuccess) {
                    Log.d("ttt", "connect: Could not connect")
                } else {
                    _isConnected.value = true
                }
            }
        )
    }

    private fun sendCommand(command: String) {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket!!.outputStream.write(command.toByteArray())
            } catch (e: IOException) {
                Log.d("ttt", "sendCommand: Could Not send command")
                e.printStackTrace()
            }
        }
    }

    //failed experiment
    /*private fun receiveData(socket: BluetoothSocket?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                while (true){
                    val available = socket!!.inputStream.available()
                    val bytes = ByteArray(available)
                    socket.inputStream.read(bytes, 0, available)
                    val text = String(bytes)
                    Log.i("server", text)
                }
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }*/


    fun disconnect() {
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket!!.close()
                bluetoothSocket = null
                _isConnected.value = false

                Log.d("ttt", "disconnect: Disconnected...")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


}