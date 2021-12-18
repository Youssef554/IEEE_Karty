package com.example.karty.presentation.utils

import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.karty.R
import com.example.karty.presentation.startScreen.MainActivity

object Helpers {

    //did not use....
    fun showLoadingIndicator(progressBar: ProgressBar, state:Boolean){
        progressBar.visibility = if (state) ProgressBar.VISIBLE else ProgressBar.GONE
    }

    fun showIsBluetoothOn(activity: MainActivity, state: Boolean){
        val tv1:TextView = activity.findViewById(R.id.textView)
        val tv2:TextView = activity.findViewById(R.id.textView2)
        val btn:Button = activity.findViewById(R.id.btn_RetryButton)
        tv1.visibility = if (state) TextView.VISIBLE else TextView.GONE
        tv2.visibility = if (state) TextView.VISIBLE else TextView.GONE
        btn.visibility = if (state) Button.VISIBLE else Button.GONE

    }

    fun String.filterBluetoothMessages():List<Int>{
        //moving left
        if (this.contains("two motors are moving forward in a speed of (3 volts)")){
            return listOf(3, 3)
        }else if (this.contains("two motors are moving forward in full speed (5 volts)")){
            return listOf(5, 5)
        }else if (this.contains("two motors are moving backward in full speed")){
            return listOf(-5,-5)
        }else if (this.contains("left motor is moving left in low speed (3 volt)")){
            return listOf(3, 5)
        }else if (this.contains("left motor is moving right in full speed (5 volt)")){
            return listOf(5, 3)
        }else{
            return listOf(0, 0)
        }
    }
    
    enum class AdapterClickActionType{
        NORMAL_CLICK,
        DATABASE_CLICK
    }
}