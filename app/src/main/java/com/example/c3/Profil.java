package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Profil extends AppCompatActivity {

    public Button ok, cancel;
    public EditText username, name, surname, password, passwordConfirm;
    int userId;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);

        userId = getIntent().getIntExtra("userId", 0);

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        password = findViewById(R.id.password);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        ok = findViewById(R.id.ok);
        cancel = findViewById(R.id.cancel);

        DB = new DBHelper(this);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_ = username.getText().toString();
                String name_ = name.getText().toString();
                String surname_ = surname.getText().toString();
                String password_ = password.getText().toString();
                String passwordConfirm_ = passwordConfirm.getText().toString();

                if(!username_.isEmpty() && !name_.isEmpty() && !surname_.isEmpty() && !password_.isEmpty() && !passwordConfirm_.isEmpty() && password_.equals(passwordConfirm_)) {
                    Boolean checkInsertData = DB.updateUser(username_, name_, surname_, password_, userId);
                    if(checkInsertData) {
                        Intent intent = new Intent (Profil.this, MainActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (Profil.this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        displaydata();
    }

    private void displaydata() {
        Cursor cursor = DB.getAllLists("User");
        while(cursor.moveToNext())
        {
            if(cursor.getInt(0) == userId) {
                username.setText(cursor.getString(1));
                name.setText(cursor.getString(2));
                surname.setText(cursor.getString(3));
                password.setText(cursor.getString(4));
                passwordConfirm.setText(cursor.getString(4));
            }
        }
    }
}