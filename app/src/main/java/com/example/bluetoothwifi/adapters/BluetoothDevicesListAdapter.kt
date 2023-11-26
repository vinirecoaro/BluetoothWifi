package com.example.bluetoothwifi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothwifi.R

class BluetoothDevicesListAdapter(private var data : MutableList<String>) : RecyclerView.Adapter<BluetoothDevicesListAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val deviceName : TextView = itemView.findViewById(R.id.tv_bluetooth_device_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_device_item, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.deviceName.text = item
    }
    fun updateBluetoothDivices(bluetoothDevices : MutableList<String>){
        data = bluetoothDevices
        notifyDataSetChanged()
    }
}