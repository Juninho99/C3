package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddItem extends AppCompatActivity {

    public Button nazad;
    public Button potvrdi;
    public EditText imeArtikla;
    public EditText kolicina;
    public EditText dodatneInfo;

    public String imeListe;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        imeListe = getIntent().getStringExtra("listName");

        nazad = findViewById(R.id.nazad);
        potvrdi = findViewById(R.id.potvrdi);
        imeArtikla = findViewById(R.id.imeArtikla);
        kolicina = findViewById(R.id.kolicina);
        dodatneInfo = findViewById(R.id.dodatneInfo);

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (AddItem.this, SelectedList.class);
                startActivity(intent);
            }
        });

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ime = imeArtikla.getText().toString();
                String kol = kolicina.getText().toString();
                String info = dodatneInfo.getText().toString();

                System.out.println("+++++++++++++++++++++ " + imeListe);
                System.out.println("+++++++++++++++++++++ " + ime);
                System.out.println("+++++++++++++++++++++ " + kol);
                System.out.println("+++++++++++++++++++++ " + info);

                Boolean checkInsertData = DB.insertItem(imeListe, ime, kol, info);

                if(checkInsertData) {
                    Intent intent = new Intent(AddItem.this, SelectedList.class);
                    startActivity(intent);
                }
            }
        });
    }
}