package com.example.bluetoothwifi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothwifi.R

class WiFiConnectionsListAdapter(private var data : MutableList<String>) : RecyclerView.Adapter<WiFiConnectionsListAdapter.ViewHolder>() {
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val wifiConnection : TextView = itemView.findViewById(R.id.tv_wifi_connection_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.wifi_connection_item,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.wifiConnection.text = item
    }
    fun updateWifiConnectins(wifiConnections : MutableList<String>){
        data = wifiConnections
        notifyDataSetChanged()
    }
}