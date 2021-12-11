package com.example.karty.presentation.turnOnBluetoothScreen

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.karty.R
import com.example.karty.presentation.startScreen.MainActivity
import com.example.karty.presentation.utils.Helpers

class TurnOnBluetoothActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_turn_on_bluetooth)



        val bluetoothManger = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManger.adapter

        if (bluetoothAdapter?.isEnabled == true) {
            goToMainActivity()
        }else {
            handleBluetoothOff()
        }


    }



    private fun handleBluetoothOff() {
        val turnOnBluetoothActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                if (it.resultCode == Activity.RESULT_OK){
                    Toast.makeText(this, "Bluetooth is on", Toast.LENGTH_SHORT).show()
                    goToMainActivity()
                }else if (it.resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(this, "mission failed :(", Toast.LENGTH_SHORT).show()
                }
            }
        val btn: Button = findViewById(R.id.btn_RetryButton)
        btn.setOnClickListener {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            turnOnBluetoothActivity.launch(intent)
        }
    }

    fun goToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}


