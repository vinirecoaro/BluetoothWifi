import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bluetoothwifi.MainActivity

class BluetoothHandler(private val context: Context, private val bluetoothAdapter: BluetoothAdapter) {

    // Método para descobrir dispositivos Bluetooth ao redor
    fun discoverDevices(): Set<BluetoothDevice> {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADMIN)
            == PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter.startDiscovery()
            return bluetoothAdapter.bondedDevices
        } else {
            // Se a permissão não foi concedida, você pode lidar com isso aqui ou solicitar a permissão ao usuário.
            // Por exemplo, solicitar permissão usando um diálogo.
            // Implemente o código para solicitar a permissão ao usuário.
            return emptySet()
        }
    }


}
