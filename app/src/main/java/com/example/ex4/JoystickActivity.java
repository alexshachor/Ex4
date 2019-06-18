package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class JoystickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.updateAileron(0.2);
        clientHandler.updateElevator(0.97);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//TODO close connection on destroy, check if changing view to landscape view calls onDestroy
    }
}
