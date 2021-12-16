package com.example.karty.presentation.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.karty.R
import javax.inject.Inject


class SharedPresManger @Inject constructor(private val context: Context) {
    fun getSharedPref(): SharedPreferences? {
        val sharedPrefName = context.getString(R.string.shared_prefs)
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
    }
}