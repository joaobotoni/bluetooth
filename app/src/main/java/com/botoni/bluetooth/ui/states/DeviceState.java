package com.botoni.bluetooth.ui.states;

public final class DeviceState {
    private final String name;
    private final String address;
    private final Bond bond;
    private final boolean connected;

    public DeviceState(String name, String address, Bond bond, boolean connected) {
        this.name = name;
        this.address = address;
        this.bond = bond;
        this.connected = connected;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Bond getBond() {
        return bond;
    }

    public boolean isConnected() {
        return connected;
    }
}
