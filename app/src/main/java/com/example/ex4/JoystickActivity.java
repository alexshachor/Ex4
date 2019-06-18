package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class JoystickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_joystick);
        // JoystickView joystick = new JoystickView(this);
        setContentView(R.layout.activity_joystick);
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.sendCommand("setAileron", 0.333);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//TODO close connection on destroy, check if changing view to landscape view calls onDestroy
    }
}
