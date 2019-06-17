package com.example.ex4;

import android.os.AsyncTask;
import android.util.Log;

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
        Log.d("tag0", "0");

        final String finalCommand = command;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] commandBytes = finalCommand.getBytes();
                    Log.d("tag1", "1");
                    clientStream.write(commandBytes, 0, commandBytes.length);
                    clientStream.flush();
                    Log.d("tag2", "2");


                } catch (Exception e) {

                }
            }
        };
        //run the thread
        Thread thread = new Thread(runnable);
        thread.start();
    }
//        AsyncTask t = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                   // while (true) {
//                      Runnable runnable  = new Runnable() {
//                          @Override
//                          public void run() {
//                              sendCommandThread(finalCommand);
//                          }
//                      };
//
//                      Thread t = new Thread(runnable);
//                      t.start();
//
//                  //  }
//                } catch (Exception e) {
//
//                }
//
//                return null;
//                //    Runnable runnable = new Runnable() {
//                //        @Override
//                //        public void run() {
//                //            sendCommandThread(finalCommand);
//                //        }
//                //    };
//
//                //    return null;
//                // }
//            }
//        };
//        t.execute((Void[])null);
//}

    public boolean isConnected() {
        return this.isConnected;
    }

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

    private void sendCommandThread(String command) {

        byte[] commandBytes = command.getBytes();
        try {
            if (clientSocket != null) {
                clientStream.write(commandBytes, 0, commandBytes.length);
                clientStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
