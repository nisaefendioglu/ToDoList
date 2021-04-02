package com.android.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Login extends Activity {

    NetworkHelper networkHelper = new NetworkHelper();

    EditText userMail;
    EditText userPassword;
    Button signInButton;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userMail = findViewById(R.id.userMail);
        userPassword = findViewById(R.id.userPassword);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = "{\"name\": \"" + userMail.getText() + "\", \"password\":\"" + userPassword.getText() + "\"}";
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
                networkHelper.post("http://10.0.3.2:8000/smarthome/_session", json, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.body().string();
                        final String messageText = "Status code : " + response.code() +
                                "\n" +
                                "Response body : " + responseStr;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), messageText, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }
}