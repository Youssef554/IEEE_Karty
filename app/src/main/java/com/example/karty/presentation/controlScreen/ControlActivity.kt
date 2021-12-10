package com.example.karty.presentation.controlScreen

import android.R.attr
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.karty.R
import java.io.IOException
import java.util.*
import android.view.MotionEvent

import android.R.attr.button
import android.annotation.SuppressLint
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import kotlinx.coroutines.*
import java.text.FieldPosition
import kotlin.concurrent.schedule
import kotlin.math.log


class ControlActivity : AppCompatActivity() {
    private val viewModel:ControlViewModel by viewModels()
    private val DELAY = 200L
    private lateinit var deviceAddress:String
    private lateinit var deviceName:String
    var myUUID:UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")//constant value for the arduino bord
    var bluetoothSocket:BluetoothSocket? = null
    var isConnected = false
    lateinit var bluetoothAdapter: BluetoothAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        deviceName = intent.getStringExtra("deviceName")!!
        supportActionBar?.title = "Control $deviceName"
        deviceAddress= intent.getStringExtra("macAddress")!!
        connect(deviceAddress)
        //buttons
        val forewordBtn:Button = findViewById(R.id.btn_GoForeword)
        val backwardBtn:Button = findViewById(R.id.btn_GoBackward)
        val rightBtn:Button = findViewById(R.id.btn_GoRight)
        val leftBtn:Button = findViewById(R.id.btn_GoLeft)
        //monitor is connected or not
        viewModel.isConnected.observe(this){
            val tv:TextView = findViewById(R.id.tv_IsConnected)
            tv.text = if (it) "Connected" else "Not connected"
        }
        forewordBtn.setOnTouchListener { _, motionEvent ->
            moveWhileBtnPressed(motionEvent, "a")
        }
        rightBtn.setOnTouchListener { _, motionEvent ->
            moveWhileBtnPressed(motionEvent, "b")
        }

        backwardBtn.setOnTouchListener { _, motionEvent ->
            moveWhileBtnPressed(motionEvent, "c")
        }

        leftBtn.setOnTouchListener { _, motionEvent ->
            moveWhileBtnPressed(motionEvent, "d")
        }
    }




    private fun moveWhileBtnPressed(motionEvent:MotionEvent, position: String):Boolean{
        if(motionEvent.action == MotionEvent.ACTION_DOWN){
            val job = Job()
            CoroutineScope(Dispatchers.Main + job).launch {
                while (true){
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

    private fun connect(deviceAddress:String){
        var connectionSuccess = true
        lifecycleScope.executeAsyncTask(
            onPreExecute = {
                Log.d("ttt", "connect: trying to connect....")
                Toast.makeText(this, "Connecting to $deviceName", Toast.LENGTH_SHORT).show()
            },
            doInBackground = {
                try {
                    if (bluetoothSocket == null || !isConnected){
                        Log.d("ttt", "connect: trying to connect2....")
                        val bluetoothManger = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                        bluetoothAdapter = bluetoothManger.adapter
                        val device:BluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress)
                        bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID)
                        bluetoothAdapter.cancelDiscovery()
                        bluetoothSocket!!.connect()
                    }
                }catch (e:IOException){
                    connectionSuccess = false
                    Log.e("ttt", "connect: ${e.message}", )
                    e.printStackTrace()
                }
            },
            onPostExecute = {
                if (!connectionSuccess){
                    Log.d("ttt", "connect: Could not connect")
                    Toast.makeText(this, "Could not connect", Toast.LENGTH_SHORT).show()
                }else{
                    isConnected = true
                    viewModel.connectionChanged(true)
                }
            }
        )
    }

    private fun sendCommand(command:String){
        if (bluetoothSocket != null){
            try {
                bluetoothSocket!!.outputStream.write(command.toByteArray())
            }catch (e: IOException){
                Log.d("ttt", "sendCommand: Could Not send command")
                e.printStackTrace()
            }
        }
    }

    fun disconnect(){
        if (bluetoothSocket != null){
            try {
                bluetoothSocket!!.close()
                bluetoothSocket = null
                isConnected = false
                viewModel.connectionChanged(false)
                Log.d("ttt", "disconnect: Disconnected")
            }catch (e:IOException){
                e.printStackTrace()
            }
            finish()
        }
    }

    override fun onBackPressed() {
        disconnect()
        super.onBackPressed()
    }
}