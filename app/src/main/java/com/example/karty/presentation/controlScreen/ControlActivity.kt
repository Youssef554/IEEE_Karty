package com.example.karty.presentation.controlScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
        val responseRV: RecyclerView = findViewById(R.id.rv_DataMonitor)
        val forewordBtn: Button = findViewById(R.id.btn_GoForeword)
        val backwardBtn: Button = findViewById(R.id.btn_GoBackward)
        val rightBtn: Button = findViewById(R.id.btn_GoRight)
        val leftBtn: Button = findViewById(R.id.btn_GoLeft)
        setupDatabaseSwitch(isDataSavedSwitch)
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

            Log.e("ttt", "onCreate: jsfkjdkfjhdkjfhkjdshf ${it.size}")
        }


        //movement controls using a custom onTouch listener
        forewordBtn.setOnTouchListener { _, motionEvent ->
            viewModel.moveWhileBtnPressed(motionEvent, "a")
            true
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

    private fun setupRecyclerView(rv: RecyclerView) {
        adapter = DataMonitorAdapter()

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun setupDatabaseSwitch(isDataSavedSwitch: SwitchCompat) {
        viewModel.isLoggingEnabled.observe(this) {
            isDataSavedSwitch.isChecked = it

        }

        isDataSavedSwitch.setOnCheckedChangeListener { _, isChecked: Boolean ->
            if (isChecked) {
                Toast.makeText(
                    this,
                    "Data is saved to the database the app may be slow",
                    Toast.LENGTH_SHORT
                ).show()
            }
            viewModel.changeIsDataLogged(isChecked)
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