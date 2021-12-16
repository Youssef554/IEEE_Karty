package com.example.karty.presentation.readingsScreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.karty.R
import com.example.karty.presentation.utils.ReadingsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

    const val STORAGE_PERMISSION_REQUEST_CODE = 1

@AndroidEntryPoint
class ReadingsActivity : AppCompatActivity() {
    private lateinit var adapter: ReadingsAdapter
    private lateinit var viewModel: ReadingsViewModel
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
        val rv: RecyclerView = findViewById(R.id.rv_ReadingsList)
        setupRecyclerView(rv)

        viewModel.readings.observe(this) {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_readings_screen, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_ShareDatabae -> {
                exportData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

private fun exportData(){
    if (havePermissions()) {
        val f = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "readings.txt"
        )
        viewModel.exportReadings(f)
    }else{
        requestStoragePermission()
    }

}

    private fun havePermissions() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestStoragePermission(){
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(this, permissions, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            exportData()
        }else{
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()

        }
    }

}