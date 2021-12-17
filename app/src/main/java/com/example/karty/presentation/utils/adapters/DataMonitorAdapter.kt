package com.example.karty.presentation.utils.adapters

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
class DataMonitorAdapter(): ListAdapter<RcResponse, DataMonitorAdapter.DataMonitorViewHolder>( DataMonitorDiff() ) {

    inner class DataMonitorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv:TextView = itemView.findViewById(R.id.tv_DataMonitorItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataMonitorViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_motor_reading, parent, false)

        return DataMonitorViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: DataMonitorViewHolder, position: Int) {
        holder.tv.text = getItem(position).msg
    }


}

class DataMonitorDiff: DiffUtil.ItemCallback<RcResponse>(){
    override fun areItemsTheSame(oldItem: RcResponse, newItem: RcResponse): Boolean {
        return oldItem.dateTime == newItem.dateTime
    }

    override fun areContentsTheSame(oldItem: RcResponse, newItem: RcResponse): Boolean {
        return oldItem == newItem
    }

}



