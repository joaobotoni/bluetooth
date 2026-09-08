package com.botoni.bluetooth.ui.states;

public final class DeviceState {
    private final String name;
    private final String address;
    private final Status status;
    public DeviceState(String name, String address, Status status) {
        this.name = name;
        this.address = address;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Status getStatus() {
        return status;
    }
}
