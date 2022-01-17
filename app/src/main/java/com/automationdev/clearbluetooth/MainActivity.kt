package com.automationdev.clearbluetooth

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.getSimpleName()

    private lateinit var unpairButton: Button
    private lateinit var statusText: TextView
    private lateinit var pairedDevices: Set<BluetoothDevice>

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d(TAG, "Bluetooth connect permission granted")
            } else {
                Log.d(TAG, "Bluetooth connect permission NOT granted")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // BLUETOOTH_CONNECT permission need to be asked on run time on Android 12
        if (Build.VERSION.SDK_INT >= 31) {
            requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        }

        // Get paired devices
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        pairedDevices = bluetoothManager.getAdapter().bondedDevices

        // Change text
        statusText = findViewById(R.id.bl_devices_text)
        statusText.setText("There is currently ${pairedDevices.size} paired bluetooth devices.")

        // Set button to clear all when clicked
        unpairButton = findViewById(R.id.clearAllButton)
        unpairButton.setOnClickListener {
            unpairAll(pairedDevices)
        }
    }

    private fun unpairAll(pairedDevices: Set<BluetoothDevice>) {
        this.unpairButton.setEnabled(false)
        this.unpairButton.setText("Please wait...")

        var unpairCount = 0
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                try {
                    device::class.java.getMethod("removeBond").invoke(device)
                    unpairCount++

                } catch (e: Exception) {
                    Log.w(TAG,"Failed to un-pair device: ${device.address}")
                }
            }
        }

        statusText.text = String.format("${unpairCount} of ${pairedDevices.size} devices un-paired.")
        this.unpairButton.setText("Unpair All")
        this.unpairButton.setEnabled(true)
    }
}