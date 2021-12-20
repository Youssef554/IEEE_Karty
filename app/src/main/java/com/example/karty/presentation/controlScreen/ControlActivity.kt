package com.example.karty.presentation.controlScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karty.R
import com.example.karty.presentation.utils.adapters.DataMonitorAdapter
import com.example.karty.presentation.utils.adapters.ReadingsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception


@AndroidEntryPoint
class ControlActivity : AppCompatActivity() {
    private val viewModel: ControlViewModel by viewModels()
    private lateinit var deviceAddress: String
    private lateinit var deviceName: String
    private lateinit var adapter: DataMonitorAdapter


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        deviceName = intent.getStringExtra("deviceName")!!
        supportActionBar?.title = "Control $deviceName"
        deviceAddress = intent.getStringExtra("macAddress")!!
        viewModel.connect(deviceAddress, deviceName)

        //Declarations
        val isDataSavedSwitch: SwitchCompat = findViewById(R.id.sw_IsDataSaved)
        val receivingSwitch:SwitchCompat = findViewById(R.id.sw_IsReceivingEnabled)
        val speedController:SeekBar = findViewById(R.id.sb_SpeedControl)
        val responseRV: RecyclerView = findViewById(R.id.rv_DataMonitor)
        val stopBtn:Button = findViewById(R.id.btn_Stop)
        val forewordBtn: Button = findViewById(R.id.btn_GoForeword)
        val backwardBtn: Button = findViewById(R.id.btn_GoBackward)
        val rightBtn: Button = findViewById(R.id.btn_GoRight)
        val leftBtn: Button = findViewById(R.id.btn_GoLeft)

        //configurations
        setupDatabaseSwitch(isDataSavedSwitch, receivingSwitch)
        setupReceivingSwitch(receivingSwitch, receivingSwitch)
        setupRecyclerView(responseRV)

        //monitor is connected or not
        viewModel.isConnected.observe(this) {
            val tv: TextView = findViewById(R.id.tv_IsConnected)
            tv.text = if (it) "Connected" else "Not connected"
        }

        viewModel.response.observe(this) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
            if (it.size > 0){
                responseRV.smoothScrollToPosition(it.count()-1)
            }

        }


        //movement controls using a custom onTouch listener
        forewordBtn.setOnClickListener {
            Log.d("ttt", "onCreate: moving foreword")
            viewModel.move("a")
        }
        backwardBtn.setOnClickListener {
            Log.d("ttt", "onCreate: moving backward")
            viewModel.move("c")
        }
        rightBtn.setOnClickListener {
            Log.d("ttt", "onCreate: moving right")
            viewModel.move("d")
        }
        leftBtn.setOnClickListener {
            Log.d("ttt", "onCreate: moving left")
            viewModel.move("b")
        }

        //stop button
        stopBtn.setOnClickListener {
            Log.d("ttt", "onCreate: car stopping")
            viewModel.sendCommand("e")
        }

    }

    private fun setupRecyclerView(rv: RecyclerView) {
        adapter = DataMonitorAdapter()

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun setupDatabaseSwitch(isDataSavedSwitch: SwitchCompat, receivingSwitch: SwitchCompat) {
        viewModel.isLoggingEnabled.observe(this) {
            isDataSavedSwitch.isChecked = it
        }

        isDataSavedSwitch.setOnCheckedChangeListener { _, isChecked: Boolean ->
            if (isChecked) {
                Toast.makeText(
                    this,
                    "receiving data enabled to store data...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.changeIsDataLogged(isChecked)
        }

    }

    private fun setupReceivingSwitch(isReceivingSwitch: SwitchCompat, isDataSavedSwitch: SwitchCompat) {
        viewModel.isLoggingEnabled.observe(this) {
            isReceivingSwitch.isChecked = it
        }

        isReceivingSwitch.setOnCheckedChangeListener { _, isChecked: Boolean ->
            if (isChecked) {
                Toast.makeText(
                    this,
                    "receiving data...",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    this,
                    "receiving data disabled",
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.changeReceivingState(isChecked)
        }

    }

    //to disconnect in the case of the app closing.
    override fun onDestroy() {
        viewModel.disconnect()
        viewModel.isConnected.observe(this) {
            if (viewModel.isConnected.value == false) {
                Toast.makeText(this, "Disconnected...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Disconnection failed", Toast.LENGTH_SHORT).show()
            }
        }
        super.onDestroy()
    }

    //to terminate the connection on exit.
    override fun onBackPressed() {
        viewModel.disconnect()
        viewModel.isConnected.observe(this) {
            if (viewModel.isConnected.value == false) {
                Toast.makeText(this, "Disconnected...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Disconnection failed", Toast.LENGTH_SHORT).show()
            }
        }
        super.onBackPressed()
    }
}