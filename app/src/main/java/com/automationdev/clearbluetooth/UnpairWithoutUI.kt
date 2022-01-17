package com.automationdev.clearbluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.lang.Exception

class UnpairWithoutUI : AppCompatActivity() {
    private val TAG: String = UnpairWithoutUI::class.java.getSimpleName()
    private lateinit var pairedDevices: Set<BluetoothDevice>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unpair_wihtout_ui)

        // Get paired devices
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        pairedDevices = bluetoothManager.getAdapter().bondedDevices

        // Change text
        Log.w(TAG, "There is currently ${pairedDevices.size} paired bluetooth devices.")
        unpairAll(pairedDevices)
        finish()
    }

    private fun unpairAll(pairedDevices: Set<BluetoothDevice>) {
        Log.d(TAG, "Please wait while unpairing devices...")

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

        Log.w(TAG, "${unpairCount} of ${pairedDevices.size} devices un-paired.")
    }
}