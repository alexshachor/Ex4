package com.example.ex4;


import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {


    private Socket clientSocket;
    private boolean isConnected;
    private OutputStream clientStream;


    private Client() {
    }

    private static class ClientSingleton {
        private static final Client INSTANCE = new Client();
    }


    public static Client getInstance() {
        return ClientSingleton.INSTANCE;
    }

    public void connectToServer(final String ip, final int port) {

        //start connection with the server, in a separated thread
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket(ip, port);
                    clientStream = clientSocket.getOutputStream();
                    isConnected = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    synchronized public void sendCommand(String command) {

        if (!this.isConnected) {
            return;
        }

        //the simulator expects the command string to end with new-line.
        String newLine = "\r\n";
        if (!command.endsWith(newLine)) {
            command += newLine;
        }

        final String finalCommand = command;

        //send a message to the server, in a separated thread
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] commandBytes = finalCommand.getBytes();
                    clientStream.write(commandBytes, 0, commandBytes.length);
                    clientStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        //start the thread
        Thread thread = new Thread(runnable);
        thread.start();
    }


    public boolean isConnected() {
        return this.isConnected;
    }

    //close resources and stop connection with the server
    public void stopConnection() {

        if (clientSocket != null) {
            try {
                this.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isConnected = false;
        }
    }
}
