package com.botoni.bluetooth.service.classic;

import static android.content.ContentValues.TAG;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Consumer;

public class ConnectedThread extends Thread {
    private final BluetoothSocket socket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final Consumer<byte[]> onRead;
    private final Consumer<byte[]> onWrite;

    public ConnectedThread(BluetoothSocket socket, Consumer<byte[]> onRead, Consumer<byte[]> onWrite) {
        this.socket = socket;
        this.onRead = onRead;
        this.onWrite = onWrite;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }
        this.mmInStream = tmpIn;
        this.mmOutStream = tmpOut;
    }

    @Override
    public void run() {
        byte[] mmBuffer = new byte[1024];
        int numBytes;
        while (true) {
            try {
                numBytes = mmInStream.read(mmBuffer);
                if (numBytes < 0){
                    break;
                }
                onRead.accept(Arrays.copyOf(mmBuffer, numBytes));
            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        if (mmOutStream == null){
            return;
        }
        try {
            mmOutStream.write(bytes);
            onWrite.accept(bytes);
        } catch (IOException e) {
            Log.e(TAG, "Erro ao enviar dados", e);
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
