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


//the adapter is working fine don't mess with this file....
class DevicesAdapter(
    val onItemClick:(name:String, macAddress:String) -> Unit
): ListAdapter<BluetoothDevice, DevicesAdapter.DeviceViewHolder>( DevicesDiff() ) {
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
        val name = getItem(position).name
        val macAddress = getItem(position).address
        holder.name.text = name
        holder.macAddress.text = macAddress
        holder.card.setOnClickListener {
            onItemClick(name, macAddress)
        }
    }
}

class DevicesDiff:DiffUtil.ItemCallback<BluetoothDevice>(){
    override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
        return oldItem.address == newItem.address
    }

    override fun areContentsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
        return oldItem == newItem
    }

}



