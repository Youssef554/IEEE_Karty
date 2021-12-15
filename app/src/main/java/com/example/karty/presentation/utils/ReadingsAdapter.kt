package com.example.karty.presentation.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.karty.R
import com.example.karty.domain.model.RcResponse


//the adapter is working fine don't mess with this file....
class ReadingsAdapter(): ListAdapter<RcResponse, ReadingsAdapter.ReadingViewHolder>( ReadingDiff() ) {
    inner class ReadingViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val time :TextView = itemView.findViewById(R.id.tv_ReadingTime)
        val rightMotor :TextView = itemView.findViewById(R.id.tv_RightMotor)
        val leftMotor:TextView = itemView.findViewById(R.id.tv_LeftMotor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_motor_reading, parent, false)

        return ReadingViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ReadingViewHolder, position: Int) {
        holder.time.text = getItem(position).dateTime
        holder.leftMotor.text = "${getItem(position).motorLeft} volts"
        holder.rightMotor.text = "${getItem(position).motorRight} volts"

    }


}

class ReadingDiff:DiffUtil.ItemCallback<RcResponse>(){
    override fun areItemsTheSame(oldItem: RcResponse, newItem: RcResponse): Boolean {
        return oldItem.dateTime == newItem.dateTime
    }

    override fun areContentsTheSame(oldItem: RcResponse, newItem: RcResponse): Boolean {
        return oldItem == newItem
    }

}



