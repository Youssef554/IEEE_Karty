package com.example.karty.presentation.utils

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
}