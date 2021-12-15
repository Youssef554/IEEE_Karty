package com.example.karty.presentation.savedDevicesScreen

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.karty.data.data_source.RcDao
import com.example.karty.domain.model.RC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SavedDevicesViewModel @Inject constructor(private val dao: RcDao) : ViewModel() {
    private val _devices: MutableLiveData<List<RC>> = MutableLiveData(emptyList())
    val devices: LiveData<List<RC>> = _devices

    init {
        getDevices()
    }


    fun getDevices() {
        viewModelScope.launch {


            _devices.value = dao.getDevices()
        }
    }
}