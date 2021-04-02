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

public class SignUp  extends Activity {

    NetworkHelper networkHelper = new NetworkHelper();

    EditText userMail;
    EditText userPassword;
    EditText confirmPasswordInput;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        userMail = findViewById(R.id.userMail);
        userPassword =  findViewById(R.id.userPassword);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
                //şifre yanlış ise
                if (!userPassword.getText().toString().equals(confirmPasswordInput.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "The passwords do not match", Toast.LENGTH_LONG).show();
                } else {
                //şifre doğrulaması ve giriş
                    String json = "{\"name\": \"" + userMail.getText() + "\", \"password\":\"" + userPassword.getText() + "\"}";
                    networkHelper.post("http://10.0.3.2:8000/signup", json, new Callback() {
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
            }
        });
}
}