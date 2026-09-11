package com.botoni.bluetooth.ui;

import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.botoni.bluetooth.R;
import com.botoni.bluetooth.broadcast.BluetoothBroadcastReceiver;
import com.botoni.bluetooth.ui.fragments.DevicesFragment;
import com.botoni.bluetooth.ui.fragments.DevicesPairedFragment;
import com.botoni.bluetooth.ui.fragments.MainFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private NavigationBarView navigationBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup(savedInstanceState);
    }

    private void setup(Bundle savedInstanceState){
        setupInsets();
        setupNavigation(savedInstanceState);
    }

    private void setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });
    }

    public void setupNavigation(Bundle savedInstanceState) {
        navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.discover) {
                replace(new DevicesFragment());
                return true;
            }

            if (item.getItemId() == R.id.paired) {
                replace(new DevicesPairedFragment());
                return true;
            }

            if (item.getItemId() == R.id.read) {
                replace(new MainFragment());
                return true;
            }

            return false;
        });

        if (savedInstanceState == null) {
            navigationBarView.setSelectedItemId(R.id.read);
        }
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationBarView = null;
    }
}