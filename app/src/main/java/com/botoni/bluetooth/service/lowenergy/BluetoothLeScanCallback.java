package com.botoni.bluetooth.service.lowenergy;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;

import java.util.function.Consumer;

public class BluetoothLeScanCallback extends ScanCallback {
    private final Consumer<ScanResult> consumer;
    public BluetoothLeScanCallback(Consumer<ScanResult> consumer) {
        this.consumer = consumer;
    }
    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        consumer.accept(result);
    }
}
