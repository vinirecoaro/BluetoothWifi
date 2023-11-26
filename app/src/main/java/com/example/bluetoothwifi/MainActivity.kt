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


class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothHandler: BluetoothHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            // Se o dispositivo não suportar Bluetooth
            Toast.makeText(this, "Bluetooth não está disponível neste dispositivo", Toast.LENGTH_SHORT).show()
        } else {
            // Verifica e solicita permissão se necessário
            checkBluetoothPermissions()
        }

    }

    private fun checkBluetoothPermissions() {
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
        val adminPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED ||
            adminPermissionCheck != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_BLUETOOTH_PERMISSIONS
            )
        } else {
            startBluetoothDiscovery()
        }
    }

    private fun startBluetoothDiscovery() {
        bluetoothHandler = BluetoothHandler(this, bluetoothAdapter)

        GlobalScope.launch(Dispatchers.Main) {
            val devices = bluetoothHandler.discoverDevices()

            if (devices.isNotEmpty()) {
                // Dispositivos Bluetooth encontrados
                for (device in devices) {
                    // Faça o que você quiser com cada dispositivo encontrado
                    // Exemplo: Adicionar os dispositivos a uma lista ou exibir informações
                    // sobre os dispositivos na tela
                }
            } else {
                // Nenhum dispositivo Bluetooth encontrado
                Toast.makeText(this@MainActivity, "Nenhum dispositivo Bluetooth disponível", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val REQUEST_BLUETOOTH_PERMISSIONS = 101
    }

}