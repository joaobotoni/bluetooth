package com.botoni.bluetooth.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.botoni.bluetooth.R;
import com.botoni.bluetooth.ui.states.DeviceState;

import java.util.List;

public class DevicesPairedAdapter extends RecyclerView.Adapter<DevicesPairedAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onClick(DeviceState device);
    }

    private final List<DeviceState> devices;
    private final OnItemClickListener onItemClickListener;

    public DevicesPairedAdapter(List<DeviceState> devices, OnItemClickListener onItemClickListener) {
        this.devices = devices;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceState device = devices.get(position);
        holder.bind(device);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onClick(device));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_device_name);
            address = itemView.findViewById(R.id.text_device_address);
        }

        void bind(DeviceState device) {
            name.setText(device.getName());
            address.setText(device.getAddress());
        }
    }
}