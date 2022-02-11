package com.automationdev.clearbluetooth

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val TAG: String = MainActivity::class.java.getSimpleName()

    // Elements
    private lateinit var unpairButton: Button
    private lateinit var statusText: TextView
    private lateinit var pairedDevicesListView: ListView
    private lateinit var unpairedDevicesListView: ListView
    private lateinit var pairedDevices: Set<BluetoothDevice>

    // Variables
    private var pairedDevicesList: MutableList<String> = mutableListOf()
    private var unpairedDevicesList: MutableList<String> = mutableListOf()
    private lateinit var arrayAdapterPaired: ArrayAdapter<String>
    private lateinit var arrayAdapterUnpaired: ArrayAdapter<String>

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

        // Get names for paired devices
        for (device in pairedDevices) {
            pairedDevicesList.add(device.address)
        }

        // Set adapters
        pairedDevicesListView = findViewById(R.id.pairedDevices)
        arrayAdapterPaired = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.pairedDevicesList)
        pairedDevicesListView.adapter = arrayAdapterPaired



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
        // empty list
        this.unpairedDevicesList = mutableListOf()

        // Set unpair button
        this.unpairButton.setEnabled(false)
        this.unpairButton.setText("Please wait...")

        var unpairCount = 0
        if (pairedDevices.isNotEmpty()) {
            for (device in pairedDevices) {
                try {
                    device::class.java.getMethod("removeBond").invoke(device)
                    unpairCount++
                    this.unpairedDevicesList.add(device.address)
                    this.pairedDevicesList.remove(device.address)

                } catch (e: Exception) {
                    Log.w(TAG,"Failed to un-pair device: ${device.address}")
                }
            }
            // Show unpaired devices on the list
            this.arrayAdapterPaired.notifyDataSetChanged()
            unpairedDevicesListView = findViewById(R.id.unpairedDevices)
            arrayAdapterUnpaired = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, this.unpairedDevicesList)
            unpairedDevicesListView.adapter = arrayAdapterUnpaired
        }

        statusText.text = String.format("${unpairCount} of ${pairedDevices.size} devices un-paired.")
        this.unpairButton.setText("Unpair All")
        this.unpairButton.setEnabled(true)
    }
}