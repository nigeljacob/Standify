package com.nnjtrading.standify;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AndroidAppClient extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {

        String serverAddress = "192.168.1.7"; // Replace with your Mac's IP
        int portNumber = 8080;
        String command = strings[0];

        try {
            Socket socket = new Socket(serverAddress, portNumber);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(command);

            socket.close();
        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }

        return null;
    }
}
