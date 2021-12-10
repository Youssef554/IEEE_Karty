package com.example.karty.presentation.startScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartScreenViewModel:ViewModel() {
    private val _isBluetoothOn:MutableLiveData<Boolean> = MutableLiveData(false)
    val isBluetoothOn: LiveData<Boolean> = _isBluetoothOn





}