package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    public Button potvrdi, registracija;
    public EditText username;
    public EditText password;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        potvrdi = findViewById(R.id.potvrdi);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registracija = findViewById(R.id.registracija);

        DB = new DBHelper(this);

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                Boolean checkInsertData = DB.checkUser(user, pass);

                if(checkInsertData) {
                    Intent intent = new Intent (Login.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}