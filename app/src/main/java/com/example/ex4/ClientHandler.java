package com.example.ex4;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class ClientHandler {

    private Map<String, String> commandsMap;

    public ClientHandler() {
        commandsMap = new HashMap<String, String>();
        commandsMap.put("setAileron", "set controls/flight/aileron ");
        commandsMap.put("setElevator", "set controls/flight/elevator ");
    }

    public void ConnectToServer(String ip, int port) {
        if (!ip.isEmpty()) {
            if (Client.getInstance().isConnected()) {
                this.CloseConnection();
            }
            Client.getInstance().connectToServer(ip, port);
        }
    }

    public void CloseConnection() {
        Client.getInstance().stopConnection();
    }

    public void sendCommand(String key, double value) {

        Client client = Client.getInstance();

        if (client.isConnected()) {
            String command = this.commandsMap.get(key) + value;
            client.sendCommand(command);
        }

    }


}
