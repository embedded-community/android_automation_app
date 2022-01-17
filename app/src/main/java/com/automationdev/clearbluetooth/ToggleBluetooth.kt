package com.automationdev.clearbluetooth

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class ToggleBluetooth : AppCompatActivity() {
    private val TAG: String = UnpairWithoutUI::class.java.getSimpleName()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toggle_bluetooth)

        // Get bluetooth adapter toggle bluetooth
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

        when (getIntent().getStringExtra("bluetooth")) {
            "enable" -> {
                bluetoothManager.getAdapter().enable()
                Log.w(TAG, "Bluetooth enabled")
            }
            "disable" -> {
                bluetoothManager.getAdapter().disable()
                Log.w(TAG, "Bluetooth disabled")
            }
            else -> {
                Log.w(TAG, "Activity needs parameter 'enable'/'disable'")
            }
        }

        finish()
    }
}