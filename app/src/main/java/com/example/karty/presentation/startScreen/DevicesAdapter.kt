package com.example.karty.presentation.startScreen

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.karty.R
import com.example.karty.domain.model.RC


//the adapter is working fine don't mess with this file....
class DevicesAdapter(
    val onItemClick:(name:String, macAddress:String) -> Unit
): ListAdapter<RC, DevicesAdapter.DeviceViewHolder>( DevicesDiff() ) {
    inner class DeviceViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val card :CardView = itemView.findViewById(R.id.dc_DeviceCard)
        val name: TextView = itemView.findViewById(R.id.tv_DeviceName)
        val macAddress: TextView = itemView.findViewById(R.id.tv_DeviceMacAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)

        return DeviceViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val name = getItem(position).deviceName
        val macAddress = getItem(position).deviceAddress
        holder.name.text = name
        holder.macAddress.text = macAddress
        holder.card.setOnClickListener {
            onItemClick(name, macAddress)
        }
    }
}

class DevicesDiff:DiffUtil.ItemCallback<RC>(){
    override fun areItemsTheSame(oldItem: RC, newItem: RC): Boolean {
        return oldItem.deviceAddress == newItem.deviceAddress
    }

    override fun areContentsTheSame(oldItem: RC, newItem: RC): Boolean {
        return oldItem == newItem
    }

}



