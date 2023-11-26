package com.example.bluetoothwifi
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.core.content.ContextCompat

class WifiHandler(private val context: Context, private val wifiManager: WifiManager) {
    fun scanWifiNetworks(): List<ScanResult> {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            wifiManager.startScan()
            return wifiManager.scanResults
        } else {
            return emptyList()
        }
    }
}