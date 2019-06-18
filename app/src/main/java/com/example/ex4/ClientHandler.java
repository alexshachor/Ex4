package com.example.ex4;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ClientHandler {

    private Map<String, String> commandsMap;

    public ClientHandler() {

        //init commands map, where the value is the command itself
        commandsMap = new HashMap<String, String>();
        commandsMap.put("Aileron", "set controls/flight/aileron ");
        commandsMap.put("Elevator", "set controls/flight/elevator ");
    }

    public void connectToServer(String ip, int port) {
        if (!ip.isEmpty()) {
            //if the client is already connected, close connection
            if (Client.getInstance().isConnected()) {
                this.stopConnection();
            }
            Client.getInstance().connectToServer(ip, port);
        }
    }

    public void stopConnection() {
        Client.getInstance().stopConnection();
    }

    public void updateAileron(double value) {
        sendCommand("Aileron", value);
    }

    public void updateElevator(double value) {
        sendCommand("Elevator", value);
    }

    private void sendCommand(String key, double value) {

        if (Client.getInstance().isConnected()) {
            String command = this.commandsMap.get(key) + value;
            Client.getInstance().sendCommand(command);
        }

    }

}
