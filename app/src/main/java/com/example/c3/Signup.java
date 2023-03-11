package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Signup extends AppCompatActivity {

    public Button registracija;
    public EditText username, name, surname, password, passwordConfirm;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        registracija = findViewById(R.id.registracija);

        DB = new DBHelper(this);

        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_ = username.getText().toString();
                String name_ = name.getText().toString();
                String surname_ = surname.getText().toString();
                String password_ = password.getText().toString();
                String passwordConfirm_ = passwordConfirm.getText().toString();

                System.out.println("-------------" + username_);
                System.out.println("-------------" + name_);
                System.out.println("-------------" + surname_);
                System.out.println("-------------" + password_);
                System.out.println("-------------" + passwordConfirm_);

                System.out.println("-------------" + !username_.isEmpty());
                System.out.println("-------------" + !name_.isEmpty());
                System.out.println("-------------" + !surname_.isEmpty());
                System.out.println("-------------" + password_.isEmpty());
                System.out.println("-------------" + passwordConfirm_.isEmpty());
                System.out.println("-------------" + !surname_.isEmpty());
                if(!username_.isEmpty() && !name_.isEmpty() && !surname_.isEmpty() && !password_.isEmpty() && !passwordConfirm_.isEmpty() && password_.equals(passwordConfirm_)) {
                    Boolean checkInsertData = DB.insertUser(username_, name_, surname_, password_);
                    if(checkInsertData) {
                        Intent intent = new Intent (Signup.this, Login.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}