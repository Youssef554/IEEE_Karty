package com.example.karty.presentation.controlScreen

import android.bluetooth.BluetoothSocket
import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karty.domain.use_cases.BluetoothUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.IOException
import javax.inject.Inject
import kotlin.properties.Delegates

private const val DELAY = 900L
private lateinit var DEVICE_MAC: String

@HiltViewModel
class ControlViewModel @Inject constructor(
    private val useCases: BluetoothUseCases
) : ViewModel() {
    var bluetoothSocket: BluetoothSocket? = null

    private var _isConnected: MutableLiveData<Boolean> = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = _isConnected

    private var _text: MutableLiveData<String> = MutableLiveData("")
    val text: LiveData<String> = _text


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

    fun connect(deviceAddress: String) {
        DEVICE_MAC = deviceAddress
        var connectionSuccess = true
        viewModelScope.executeAsyncTask(
            onPreExecute = {
                Log.d("ttt", "connect: trying to connect....")
            },
            doInBackground = {
                try {
                    if (bluetoothSocket == null || !isConnected.value!!) {
                        bluetoothSocket = useCases.connect(deviceAddress)
                        Log.d("ttt", "connect: Connected")
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
                useCases.sendCommand(bluetoothSocket!!, command)
                receiveData()
            } catch (e: IOException) {
                Log.e("ttt", "sendCommand: ${e.message}")
                _isConnected.value = false
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                while (true) {
                    if (isConnected.value == false) {
                        connect(DEVICE_MAC)
                        delay(1000)
                    }

                }
            }
        }
    }

    //under construction.... somewhat functional
    private fun receiveData() {
        var bytes: Int
        val buffer = ByteArray(1024)
        var message = ""
        if (isConnected.value == true) {
            try {
                while (!(message.contains("[e]"))) {
                    //read bytes received and ins to buffer
                    bytes = bluetoothSocket!!.inputStream.read(buffer)
                    //convert to string
                    message += String(buffer, 0, bytes)
                }

                _text.value = message
                Log.d("ttt", "receiveData: ${text.value}")
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }


    fun disconnect() {
        if (bluetoothSocket != null) {
            try {
                useCases.disconnect(bluetoothSocket!!)
                Log.d("ttt", "disconnect: Disconnected")
                _isConnected.value = false
            } catch (e: IOException) {
                Log.e("ttt", "disconnect: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}