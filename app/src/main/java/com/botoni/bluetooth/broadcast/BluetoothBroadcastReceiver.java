package com.botoni.bluetooth.broadcast;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.botoni.bluetooth.ui.states.Bond;
import com.botoni.bluetooth.ui.states.DeviceState;

import java.util.function.Consumer;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {
    private final Consumer<DeviceState> consumer;
    public BluetoothBroadcastReceiver(Consumer<DeviceState> consumer) {
        this.consumer = consumer;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice.class);
            if (device == null){
                return;
            }
            consumer.accept(new DeviceState(device.getName(), device.getAddress(), bond(device), false));
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private Bond bond(BluetoothDevice device) {
        switch (device.getBondState()) {
            case BluetoothDevice.BOND_BONDED:
                return Bond.BONDED;
            case BluetoothDevice.BOND_BONDING:
                return Bond.BONDING;
            default:
                return Bond.NONE;
        }
    }
}