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
    private static final int BACK_PRESS_DELAY = 2000; // Vrijeme (u milisekundama) u kojem se očekuje dvostruki klik
    private long backPressTime; // Vrijeme pritiska na tipku "Back"
    private int backPressCount; // Broj pritisaka na tipku "Back"

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
                int checkInsertData = DB.checkUser(user, pass);

                if(checkInsertData != -1) {
                    Intent intent = new Intent (Login.this, MainActivity.class);
                    intent.putExtra("userId", checkInsertData);
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

    @Override
    public void onBackPressed() {
        if (backPressTime + BACK_PRESS_DELAY > System.currentTimeMillis()) {
            backPressCount++; // Povećaj broj pritisaka
            if (backPressCount >= 2) {
                finishAffinity(); // Završi sve aktivnosti povezane s trenutnom aktivnošću
                System.exit(0); // Ubij proces i izađi iz aplikacije
            }
        } else {
            backPressCount = 1; // Resetiraj broj pritisaka ako je prošlo više od BACK_PRESS_DELAY vremena
        }
        backPressTime = System.currentTimeMillis(); // Ažuriraj vrijeme pritiska
    }
}