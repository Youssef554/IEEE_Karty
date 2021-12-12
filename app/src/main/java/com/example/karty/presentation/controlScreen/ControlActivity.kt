package com.example.karty.presentation.controlScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.karty.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        viewModel.connect(deviceAddress)

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