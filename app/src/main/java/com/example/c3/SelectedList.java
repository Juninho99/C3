package com.example.c3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectedList extends Activity implements RecyclerViewInterface{

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    String listCode;
    Button dodaj, prikaziKodListe, nazad;
    RecyclerView recyclerView;
    ArrayList<String> editList;
    DBHelper DB;
    MyAdapter adapter;
    Integer idOfList;
    String imeListe;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_list);

        imeListe = getIntent().getStringExtra("listName");
        idOfList = getIntent().getIntExtra("idOfList", 0);
        listCode = getIntent().getStringExtra("listCode");

        userId = getIntent().getIntExtra("userId", 0);

        TextView textView = findViewById(R.id.textView);
        textView.setText(imeListe);

        dodaj = findViewById(R.id.dodaj);
        prikaziKodListe = findViewById(R.id.prikaziKodListe);
        nazad = findViewById(R.id.nazad);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (SelectedList.this, AddItem.class);
                intent.putExtra("idOfList", idOfList);
                intent.putExtra("listName", imeListe);
                intent.putExtra("listCode", listCode);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        prikaziKodListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCodeDialog();
            }
        });

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (SelectedList.this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        DB = new DBHelper(this);
        editList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(this, editList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();
    }

    private void displaydata() {
        Cursor cursor = DB.getAllLists("Article");
        while(cursor.moveToNext())
        {
            if(cursor.getInt(4) == idOfList)
                editList.add(cursor.getString(1));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (SelectedList.this, MainActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(SelectedList.this, ChangeItem.class);

        intent.putExtra("listName", imeListe);
        intent.putExtra("idOfList", idOfList);
        intent.putExtra("listCode", listCode);
        intent.putExtra("articleName", String.valueOf(editList.get(position)));
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {

    }

    public void createNewCodeDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View codePopupView = getLayoutInflater().inflate(R.layout.codepopup, null);

        TextView textView = codePopupView.findViewById(R.id.textView1);
        textView.setText(listCode);

        Button ok = codePopupView.findViewById(R.id.ok);

        dialogBuilder.setView(codePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
