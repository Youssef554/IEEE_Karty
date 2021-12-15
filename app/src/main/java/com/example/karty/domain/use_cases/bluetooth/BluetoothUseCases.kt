package com.example.karty.domain.use_cases.bluetooth

data class BluetoothUseCases(
    val connect: BluetoothConnectUseCase,
    val disconnect: BluetoothDisconnectUseCase,
    val sendCommand: BluetoothSendCommandUseCase,

    )