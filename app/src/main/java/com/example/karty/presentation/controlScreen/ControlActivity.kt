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
    private lateinit var deviceAddress:String
    private lateinit var deviceName:String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        deviceName = intent.getStringExtra("deviceName")!!
        supportActionBar?.title = "Control $deviceName"
        deviceAddress= intent.getStringExtra("macAddress")!!
        viewModel.connect(this, deviceAddress)

        //buttons declaration
        val forewordBtn:Button = findViewById(R.id.btn_GoForeword)
        val backwardBtn:Button = findViewById(R.id.btn_GoBackward)
        val rightBtn:Button = findViewById(R.id.btn_GoRight)
        val leftBtn:Button = findViewById(R.id.btn_GoLeft)
        //monitor is connected or not
        viewModel.isConnected.observe(this){
            val tv:TextView = findViewById(R.id.tv_IsConnected)
            tv.text = if (it) "Connected" else "Not connected"
        }


        //movement controls using a custom onTouch listener
        forewordBtn.setOnTouchListener { _, motionEvent ->
            viewModel.moveWhileBtnPressed(motionEvent, "a")
        }

        rightBtn.setOnTouchListener { _, motionEvent ->
            viewModel.moveWhileBtnPressed(motionEvent, "b")
        }

        backwardBtn.setOnTouchListener { _, motionEvent ->
            viewModel.moveWhileBtnPressed(motionEvent, "c")
        }

        leftBtn.setOnTouchListener { _, motionEvent ->
            viewModel.moveWhileBtnPressed(motionEvent, "d")
        }
    }






    //to terminate the connection on exit.
    override fun onBackPressed() {
        viewModel.disconnect()
        super.onBackPressed()
    }
}