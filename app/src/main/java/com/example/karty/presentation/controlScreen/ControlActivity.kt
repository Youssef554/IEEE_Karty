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
        val receivingSwitch: SwitchCompat = findViewById(R.id.sw_IsReceivingEnabled)
        val speedController: SeekBar = findViewById(R.id.sb_SpeedControl)
        val responseRV: RecyclerView = findViewById(R.id.rv_DataMonitor)
        val stopBtn: Button = findViewById(R.id.btn_Stop)
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
            if (it.size > 0) {
                responseRV.smoothScrollToPosition(it.count() - 1)
            }

        }


        //movement controls using a custom onTouch listener

        //    case 'b':forward(122);break; // 2 Volt forward
        //    case 'c':forward(153);break; // 3 Volt forward
        //    case 'd':forward(204);break; // 4 Volt forward
        //    case 'e':forward(255);break; // 5 Volt forward
        forewordBtn.setOnClickListener {
            Log.d("ttt", "onCreate: moving foreword")
            when (speedController.progress) {
                2 -> viewModel.move("b")
                3 -> viewModel.move("c")
                4 -> viewModel.move("d")
                5 -> viewModel.move("e")
                else -> viewModel.move("b")
            }
        }

        //    case 'g':right(122);break; // 2 Volt right
        //    case 'h':right(153);break; // 3 Volt right
        //    case 'i':right(204);break; // 4 Volt right
        //    case 'j':right(255);break; // 5 Volt right
        backwardBtn.setOnClickListener {
            Log.d("ttt", "onCreate: moving backward")
            when (speedController.progress) {
                2 -> viewModel.move("g")
                3 -> viewModel.move("h")
                4 -> viewModel.move("i")
                5 -> viewModel.move("j")
                else -> viewModel.move("g")
            }
        }

        //    case 'l':backward(122);break; // 2 Volt backward
        //    case 'm':backward(153);break; // 3 Volt backward
        //    case 'n':backward(204);break; // 4 Volt backward
        //    case 'o':backward(255);break; // 5 Volt backward
        rightBtn.setOnClickListener {
            Log.d("ttt", "onCreate: moving right")
            when (speedController.progress) {
                2 -> viewModel.move("l")
                3 -> viewModel.move("m")
                4 -> viewModel.move("n")
                5 -> viewModel.move("o")
                else -> viewModel.move("l")
            }
        }

        //    case 'q':left(122);break; // 2 Volt left
        //    case 'r':left(153);break; // 3 Volt left
        //    case 's':left(204);break; // 4 Volt left
        //    case 't':left(255);break; // 5 Volt left
        leftBtn.setOnClickListener {
            Log.d("ttt", "onCreate: moving left")
            when(speedController.progress){
                2 -> viewModel.move("q")
                3 -> viewModel.move("r")
                4 -> viewModel.move("s")
                5 -> viewModel.move("t")
                else -> viewModel.move("q")
            }
        }

        //stop button
        stopBtn.setOnClickListener {
            Log.d("ttt", "onCreate: car stopping")
            viewModel.sendCommand("u")
        }

    }

    private fun setupRecyclerView(rv: RecyclerView) {
        adapter = DataMonitorAdapter()

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun setupDatabaseSwitch(
        isDataSavedSwitch: SwitchCompat,
        receivingSwitch: SwitchCompat
    ) {
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

    private fun setupReceivingSwitch(
        isReceivingSwitch: SwitchCompat,
        isDataSavedSwitch: SwitchCompat
    ) {
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
            } else {
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