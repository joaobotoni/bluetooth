package com.botoni.bluetooth.service.classic;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Consumer;

public class ConnectThread extends Thread {
    private static final String TAG = "ConnectThread";
    private final Handler main = new Handler(Looper.getMainLooper());
    private final BluetoothAdapter adapter;
    private final BluetoothSocket socket;
    private final Consumer<BluetoothSocket> onConnected;
    private final Consumer<IOException> onFailure;

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public ConnectThread(@NonNull BluetoothAdapter adapter, @NonNull BluetoothDevice device, @NonNull UUID uuid,
                         @NonNull Consumer<BluetoothSocket> onConnected,
                         @NonNull Consumer<IOException> onFailure) throws IOException {
        this.adapter = adapter;
        this.onConnected = onConnected;
        this.onFailure = onFailure;
        this.socket = device.createRfcommSocketToServiceRecord(uuid);
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN})
    public void run() {
        adapter.cancelDiscovery();
        try {
            socket.connect();
            main.post(() -> onConnected.accept(socket));
        } catch (IOException e) {
            close();
            main.post(() -> onFailure.accept(e));
        }
    }

    public void cancel() {
        close();
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Não foi possível fechar o socket", e);
        }
    }
}