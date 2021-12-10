package com.example.karty.presentation.controlScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ControlViewModel:ViewModel() {
    private var _isConnected:MutableLiveData<Boolean> = MutableLiveData(false)
    val isConnected:LiveData<Boolean> = _isConnected


    fun connectionChanged(isDeviceConnected:Boolean){
        _isConnected.value = isDeviceConnected
    }



}