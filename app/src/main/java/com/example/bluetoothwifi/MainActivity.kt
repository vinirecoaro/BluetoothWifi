package com.example.bluetoothwifi

import BluetoothHandler
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.Manifest
import android.R
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bluetoothwifi.adapters.BluetoothDevicesListAdapter
import com.example.bluetoothwifi.adapters.WiFiConnectionsListAdapter
import com.example.bluetoothwifi.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter : BluetoothAdapter
    private lateinit var bluetoothHandler : BluetoothHandler
    private lateinit var wifiManager : WifiManager
    private lateinit var wifiHandler : WifiHandler
    private lateinit var bluetoothDevicesListAdapter : BluetoothDevicesListAdapter
    private lateinit var wifiConnectionsListAdapter : WiFiConnectionsListAdapter

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val bluetoothDevices : MutableList<String> = mutableListOf()
    private val wifiConnections : MutableList<String> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvBluetoothDevices.layoutManager = LinearLayoutManager(this)
        bluetoothDevicesListAdapter = BluetoothDevicesListAdapter(bluetoothDevices)
        binding.rvBluetoothDevices.adapter = bluetoothDevicesListAdapter

        binding.rvWifiConnections.layoutManager = LinearLayoutManager(this)
        wifiConnectionsListAdapter = WiFiConnectionsListAdapter(wifiConnections)
        binding.rvWifiConnections.adapter = wifiConnectionsListAdapter

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (bluetoothAdapter == null) {
            // Se o dispositivo não suportar Bluetooth
            Toast.makeText(this, "Bluetooth não está disponível neste dispositivo", Toast.LENGTH_SHORT).show()
        } else {
            // Verifica e solicita permissão se necessário
            checkBluetoothPermissions()
        }
        setUpListeners()


    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun setUpListeners(){
        binding.btSearchBluetoothDevices.setOnClickListener {
            startBluetoothDiscovery()
        }
        binding.btSearchWifiConnections.setOnClickListener {
            startWifiScan()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkBluetoothPermissions() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
        val adminPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)
        val scanPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
        val connectPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED ||
            adminPermissionCheck != PackageManager.PERMISSION_GRANTED ||
            scanPermissionCheck != PackageManager.PERMISSION_GRANTED ||
            connectPermissionCheck != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT),
                REQUEST_BLUETOOTH_PERMISSIONS
            )
        } else {
            startBluetoothDiscovery()
        }
        checkWifiPermissions()
    }

    private fun startBluetoothDiscovery() {
        bluetoothHandler = BluetoothHandler(this, bluetoothAdapter)

        GlobalScope.launch(Dispatchers.Main) {
            val devices = bluetoothHandler.discoverDevices()
            bluetoothDevices.clear()
            if (devices.isNotEmpty()) {

                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ){
                    for (device in devices) {
                        bluetoothDevices.add(device.name.toString())

                    }
                    bluetoothDevicesListAdapter.updateBluetoothDivices(bluetoothDevices)
                }


            } else {
                // Nenhum dispositivo Bluetooth encontrado
                Toast.makeText(this@MainActivity, "Nenhum dispositivo Bluetooth disponível", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkWifiPermissions() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_WIFI_PERMISSIONS
            )
        } else {
            startWifiScan()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun startWifiScan() {
        wifiHandler = WifiHandler(this, wifiManager)
        wifiConnections.clear()
        GlobalScope.launch(Dispatchers.Main) {
            val scanResults = wifiHandler.scanWifiNetworks()

            if (scanResults.isNotEmpty()) {
                for(result in scanResults){
                    wifiConnections.add(result.wifiSsid.toString().replace("\"",""))
                }
                wifiConnectionsListAdapter.updateWifiConnectins(wifiConnections)
            } else {
                // Nenhuma rede Wi-Fi encontrada
                Toast.makeText(this@MainActivity, "Nenhuma rede Wi-Fi disponível", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WIFI_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startWifiScan()
                } else {
                    Toast.makeText(this, "Permissão de Wi-Fi não concedida", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val REQUEST_BLUETOOTH_PERMISSIONS = 101
        private const val REQUEST_WIFI_PERMISSIONS = 102
    }

}