package com.example.karty.presentation.savedDevicesScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.NavUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karty.R
import com.example.karty.presentation.controlScreen.ControlActivity
import com.example.karty.presentation.startScreen.DevicesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedDevicesActivity : AppCompatActivity() {
    private lateinit var adapter: DevicesAdapter
    val viewModel: SavedDevicesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_devices)
        supportActionBar?.apply {
            title = "Devices Database"
            setDisplayHomeAsUpEnabled(true)
        }

        val rv: RecyclerView = findViewById(R.id.rv_SavedDevicesList)

        setupRecyclerView(rv)

        viewModel.devices.observe(this) {
            adapter.submitList(it)
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


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}