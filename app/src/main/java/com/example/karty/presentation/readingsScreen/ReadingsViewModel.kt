package com.example.karty.presentation.readingsScreen

import android.app.Application
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karty.data.data_source.RcDao
import com.example.karty.domain.model.RcResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import android.content.Context.MODE_PRIVATE
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter


private lateinit var DEVICE_MAC: String

@HiltViewModel
class ReadingsViewModel @Inject constructor(
    private val dao: RcDao,
    private val context: Application
) : ViewModel() {
    private val _readings: MutableLiveData<List<RcResponse>> = MutableLiveData(emptyList())
    val readings: LiveData<List<RcResponse>> = _readings


    fun getReadings(deviceAddress: String) {
        DEVICE_MAC = deviceAddress
        viewModelScope.launch {
            _readings.value = dao.getDeviceReadings(DEVICE_MAC)
        }
    }

    fun exportReadings(file: File) {
        try {
            file.writeText("")
            for (reading in readings.value!!) {
                file.appendText("${reading.dateTime} --- leftMotor: ${reading.motorLeft} --- rightMotor: ${reading.motorRight}\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}