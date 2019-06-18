package com.example.ex4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void connect(View v) {

        String ip = ((EditText) findViewById(R.id.IpField)).getText().toString();
        String portStr = ((EditText) findViewById(R.id.PortField)).getText().toString();
        try {
            int port = Integer.parseInt(portStr);

            if (!ip.isEmpty()) {
                ClientHandler clientHandler = new ClientHandler();
                clientHandler.connectToServer(ip, port);
                Intent intent = new Intent(LoginActivity.this, JoystickActivity.class);
                startActivity(intent);
            }
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
    }
}
