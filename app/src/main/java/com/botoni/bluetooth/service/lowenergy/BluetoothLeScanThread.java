package com.botoni.bluetooth.service.lowenergy;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.os.Handler;

import androidx.annotation.RequiresPermission;

import java.util.function.Consumer;


public class BluetoothLeScanThread extends Thread {
    private Handler handler;
    private boolean scanning;
    private static final long SCAN_PERIOD = 10000;
    private final BluetoothLeScanner bluetoothLeScanner;
    private final BluetoothLeScanCallback bluetoothLeScanCallback;

    public BluetoothLeScanThread(BluetoothAdapter bluetoothAdapter, Consumer<ScanResult> consumer) {
        this.bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        this.bluetoothLeScanCallback = new BluetoothLeScanCallback(consumer);
    }

    @Override
    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    public void run() {
        if (!scanning) {
            handler.postDelayed(() -> {
                scanning = false;
                bluetoothLeScanner.stopScan(bluetoothLeScanCallback);
            }, SCAN_PERIOD);

            scanning = true;
            bluetoothLeScanner.startScan(bluetoothLeScanCallback);
        } else {
            scanning = false;
            bluetoothLeScanner.stopScan(bluetoothLeScanCallback);
        }
    }

}
