package com.botoni.bluetooth.utils;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresPermission;

public class BluetoothCompat {
    private final Context context;
    private static volatile BluetoothCompat instance;
    private final BluetoothManager bluetoothManager;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothCompat(Context context) {
        this.context = context.getApplicationContext();
        this.bluetoothManager = this.context.getSystemService(BluetoothManager.class);
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

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    public void startDiscovery(){
        if(!isEnabled()){
            return;
        }
        if (isDiscovering()) {
            cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private boolean isDiscovering(){
        return bluetoothAdapter != null && bluetoothAdapter.isDiscovering();
    }
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    private void cancelDiscovery(){
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
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