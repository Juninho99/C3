package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class AddList extends AppCompatActivity {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public Button nazad;
    public Button potvrdi;
    public EditText imeListe;
    DBHelper DB;

    int userId;

    public static String generateRandomCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // Generišemo prva 2 slova
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(LETTERS.length());
            char c = LETTERS.charAt(index);
            sb.append(c);
        }

        // Generišemo prvi broj
        int number1 = random.nextInt(10);
        sb.append(number1);

        // Generišemo 1 slovo
        int index = random.nextInt(LETTERS.length());
        char c = LETTERS.charAt(index);
        sb.append(c);

        // Generišemo drugi broj
        int number2 = random.nextInt(10);
        sb.append(number2);

        // Generišemo zadnja 2 slova
        for (int i = 0; i < 2; i++) {
            int index2 = random.nextInt(LETTERS.length());
            char c2 = LETTERS.charAt(index2);
            sb.append(c2);
        }

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_list);

        nazad = findViewById(R.id.nazad);
        potvrdi = findViewById(R.id.potvrdi);
        imeListe = findViewById(R.id.imeListe);

        userId = getIntent().getIntExtra("userId", 0);

        DB = new DBHelper(this);

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (AddList.this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ime = imeListe.getText().toString();

                if(!ime.isEmpty()) {
                    Boolean checkInsertData = DB.insertList(ime, generateRandomCode());

                    if (checkInsertData) {
                        int listId = DB.getListId(ime);
                        Boolean checkInsertUserList = DB.insertUserList(userId, listId);
                        if (checkInsertUserList) {
                            Intent intent = new Intent(AddList.this, MainActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Naziv liste ne smije biti prazan", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}