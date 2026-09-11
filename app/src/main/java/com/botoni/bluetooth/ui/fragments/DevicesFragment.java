package com.botoni.bluetooth.ui.fragments;
import static androidx.core.content.ContextCompat.registerReceiver;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.botoni.bluetooth.R;
import com.botoni.bluetooth.broadcast.BluetoothBroadcastReceiver;
import com.botoni.bluetooth.service.classic.ConnectThread;
import com.botoni.bluetooth.ui.adapters.DevicesAdapter;
import com.botoni.bluetooth.ui.states.DeviceState;
import com.botoni.bluetooth.utils.BluetoothCompat;
import com.botoni.bluetooth.utils.BluetoothPermissionCompat;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;


public class DevicesFragment extends Fragment {
    private static final String TAG = "DevicesFragment";
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private DevicesAdapter devicesAdapter;
    private RecyclerView recyclerView;
    private BluetoothBroadcastReceiver receiver;
    private ConnectThread connectThread;
    private final List<DeviceState> devices = new ArrayList<>();
    private BluetoothAdapter adapter;
    private BluetoothCompat bluetoothCompat;
    private BluetoothPermissionCompat permissionCompat;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupCompat();
        setupBluetoothAdapter();
        setupReceiver(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
        setupAdapter();
        setupBluetoothAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setupDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceive(requireActivity(), receiver);
    }

    private void registerBroadcastReceive(@NonNull Activity activity, @NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        registerReceiver(activity, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    private void unregisterBroadcastReceive(@NonNull Activity activity, @NonNull BroadcastReceiver receiver) {
        activity.unregisterReceiver(receiver);
    }

    private void setupViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_devices);
    }

    private void setupCompat() {
        bluetoothCompat = BluetoothCompat.getInstance(requireContext());
        permissionCompat = BluetoothPermissionCompat.getInstance(requireContext());
    }

    private void setupReceiver(Activity activity) {
        receiver = new BluetoothBroadcastReceiver(consumer);
        registerBroadcastReceive(activity, receiver, setupIntentFilter());
    }

    private IntentFilter setupIntentFilter(){
        return new IntentFilter(BluetoothDevice.ACTION_FOUND);
    }

    private void setupAdapter() {
        devicesAdapter = new DevicesAdapter(devices, this::onDeviceClick);
        recyclerView.setAdapter(devicesAdapter);
    }

    private void setupBluetoothAdapter() {
        adapter = bluetoothCompat.getAdapter();
    }

    private void setupDestroy() {
        recyclerView = null;
        devicesAdapter = null;
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private void onDeviceClick(DeviceState state) {
        BluetoothDevice device = adapter.getRemoteDevice(state.getAddress());
        try {
            connectThread = new ConnectThread(adapter, device, SPP_UUID, this::onConnected, this::onConnectionFailure);
            connectThread.start();
        } catch (IOException e) {
            Log.e(TAG, "Falha ao criar o socket", e);
        }
    }

    private void onConnected(BluetoothSocket socket) {

    }

    private void onConnectionFailure(IOException e) {

    }
    private final Consumer<DeviceState> consumer = (deviceState) -> devices.add(deviceState);
}
