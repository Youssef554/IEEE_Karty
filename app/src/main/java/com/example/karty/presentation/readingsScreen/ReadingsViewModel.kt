package com.example.karty.presentation.readingsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karty.data.data_source.RcDao
import com.example.karty.domain.model.RC
import com.example.karty.domain.model.RcResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private lateinit var DEVICE_MAC:String

@HiltViewModel
class ReadingsViewModel @Inject constructor(private val dao: RcDao) : ViewModel() {
    private val _readings: MutableLiveData<List<RcResponse>> = MutableLiveData(emptyList())
    val devices: LiveData<List<RcResponse>> = _readings



    fun getReadings(deviceAddress:String) {
        DEVICE_MAC = deviceAddress
        viewModelScope.launch {
            _readings.value = dao.getDeviceReadings(DEVICE_MAC)
        }
    }
}