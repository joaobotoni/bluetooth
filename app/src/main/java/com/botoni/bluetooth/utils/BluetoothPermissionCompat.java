package com.botoni.bluetooth.utils;


import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class BluetoothPermissionCompat {
    private static volatile BluetoothPermissionCompat instance;
    private final Context context;

    private BluetoothPermissionCompat(Context context) {
        this.context = context.getApplicationContext();
    }

    public static BluetoothPermissionCompat getInstance(Context context) {
        if (instance == null) {
            synchronized (BluetoothPermissionCompat.class) {
                if (instance == null) {
                    instance = new BluetoothPermissionCompat(context);
                }
            }
        }
        return instance;
    }

    public boolean hasPermissions(String... permissions) {
        if (permissions == null) return true;
        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    private ActivityResultLauncher<String[]> registerPermissionsLauncher(Fragment fragment, Runnable runnable) {
        return fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                if (result.containsValue(false)) runnable.run();
        });
    }

    public void requestPermissionsLauncher(ActivityResultLauncher<String[]> launcher, String... permissions) {
        if (launcher != null && permissions != null && permissions.length > 0) {
            launcher.launch(permissions);
        }
    }

    private boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isAndroid12() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S;
    }
}
