package com.botoni.bluetooth.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

public class BluetoothCompat {
    private static volatile BluetoothCompat instance;
    private final Context context;
    private final BluetoothAdapter bluetoothAdapter;

    private BluetoothCompat(Context context) {
        this.context = context.getApplicationContext();
        BluetoothManager bluetoothManager = this.context.getSystemService(BluetoothManager.class);
        this.bluetoothAdapter = bluetoothManager != null ? bluetoothManager.getAdapter() : null;
    }
    
    public static BluetoothCompat getInstance(Context context) {
        if (instance == null) {
            synchronized (BluetoothCompat.class) {
                if (instance == null) {
                    instance = new BluetoothCompat(context);
                }
            }
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public BluetoothAdapter getAdapter() {
        return bluetoothAdapter;
    }

    public boolean isEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void requestEnable(ActivityResultLauncher<Intent> launcher) {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            launcher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
    }
}