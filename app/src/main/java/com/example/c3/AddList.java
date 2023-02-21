package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddList extends AppCompatActivity {

    public Button nazad;
    public Button potvrdi;
    public EditText imeListe;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_list);

        nazad = findViewById(R.id.nazad);
        potvrdi = findViewById(R.id.potvrdi);
        imeListe = findViewById(R.id.imeListe);

        DB = new DBHelper(this);

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (AddList.this, MainActivity.class);
                startActivity(intent);
            }
        });

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ime = imeListe.getText().toString();

                Boolean checkInsertData = DB.insertList(ime);
                String imeNoveTabele = "CREATE TABLE " + ime + "(id INTEGER PRIMARY KEY, name TEXT, quantity TEXT, description TEXT)";
                DB.makeNewTable(imeNoveTabele);

                if(checkInsertData) {
                    Intent intent = new Intent(AddList.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}