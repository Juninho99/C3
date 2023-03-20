package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeItem extends AppCompatActivity {

    public Button nazad;
    public Button potvrdi;
    public EditText imeArtikla;
    public EditText kolicina;
    public EditText dodatneInfo;
    public Integer idOfList;
    DBHelper DB;
    String listCode;
    String articleName;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_item);

        idOfList = getIntent().getIntExtra("idOfList", 0);
        String imeListe = getIntent().getStringExtra("listName");
        listCode = getIntent().getStringExtra("listCode");
        articleName = getIntent().getStringExtra("articleName");
        userId = getIntent().getIntExtra("userId", 0);

        nazad = findViewById(R.id.nazad);
        potvrdi = findViewById(R.id.potvrdi);
        imeArtikla = findViewById(R.id.imeArtikla);
        kolicina = findViewById(R.id.kolicina);
        dodatneInfo = findViewById(R.id.dodatneInfo);

        imeArtikla.setText(articleName);

        DB = new DBHelper(this);

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ChangeItem.this, SelectedList.class);
                intent.putExtra("listName", imeListe);
                intent.putExtra("idOfList", idOfList);
                intent.putExtra("listCode", listCode);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ime = imeArtikla.getText().toString();
                String kol = kolicina.getText().toString();
                String info = dodatneInfo.getText().toString();

                System.out.println("+++++++++++++++++++++ " + idOfList);
                System.out.println("+++++++++++++++++++++ " + ime);
                System.out.println("+++++++++++++++++++++ " + kol);
                System.out.println("+++++++++++++++++++++ " + info);

                Boolean checkInsertData = DB.updateList(ime, kol, info, idOfList);

                if(checkInsertData) {
                    Intent intent = new Intent(ChangeItem.this, SelectedList.class);
                    intent.putExtra("listName", imeListe);
                    intent.putExtra("idOfList", idOfList);
                    intent.putExtra("listCode", listCode);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            }
        });

        displaydata();
    }

    private void displaydata() {
        Cursor cursor = DB.getAllLists("Article");
        while(cursor.moveToNext())
        {
            if(cursor.getString(1).equals(articleName)) {
                imeArtikla.setText(articleName);
                kolicina.setText(cursor.getString(2));
                dodatneInfo.setText(cursor.getString(3));
            }
        }
    }
}