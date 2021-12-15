package com.example.karty.presentation.readingsScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karty.R
import com.example.karty.presentation.utils.ReadingsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadingsActivity : AppCompatActivity() {
    private lateinit var adapter:ReadingsAdapter
    private lateinit var viewModel:ReadingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readings)
        supportActionBar?.apply {
            title = "Readings"
            setDisplayHomeAsUpEnabled(true)
        }
        val deviceAddress = intent.getStringExtra("macAddress")
        viewModel = ViewModelProvider(this).get(ReadingsViewModel::class.java)
        if (deviceAddress != null) {
            viewModel.getReadings(deviceAddress)
        }
        val rv:RecyclerView = findViewById(R.id.rv_ReadingsList)
        setupRecyclerView(rv)

        viewModel.devices.observe(this){
            adapter.submitList(it)
        }

    }



    private fun setupRecyclerView(rv: RecyclerView) {
        adapter = ReadingsAdapter()

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }



    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}