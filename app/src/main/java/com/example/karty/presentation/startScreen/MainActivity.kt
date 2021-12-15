package com.example.karty.presentation.startScreen

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karty.R
import com.example.karty.domain.model.RC
import com.example.karty.presentation.controlScreen.ControlActivity
import com.example.karty.presentation.savedDevicesScreen.SavedDevicesActivity
import com.example.karty.presentation.utils.DevicesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var adapter: DevicesAdapter
    private val viewModel: StartScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get the recyclerview
        val devicesRecyclerView: RecyclerView = findViewById(R.id.rv_DevicesList)

        val bluetoothManger = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManger.adapter

        if (bluetoothAdapter?.isEnabled == true) {
            val btDevices = bluetoothAdapter.bondedDevices
            setupRecyclerView(devicesRecyclerView)
            val devices:MutableList<RC> = mutableListOf()
            btDevices.forEach {
                devices.add(
                    RC(deviceName = it.name, deviceAddress = it.address)
                )
            }
            adapter.submitList(devices)
        }


    }


    private fun setupRecyclerView(rv: RecyclerView) {
        adapter = DevicesAdapter() { name, macAddress ->
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra("deviceName", name)
            intent.putExtra("macAddress", macAddress)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_screen, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.btn_SavedDevices ->{
                val intent = Intent(this, SavedDevicesActivity::class.java)
                startActivity(intent)
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

}