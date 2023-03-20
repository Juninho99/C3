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
    Button dodaj, prikaziKodListe;
    RecyclerView recyclerView;
    ArrayList<String> editList;
    DBHelper DB;
    MyAdapter adapter;
    Integer idOfList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_list);

        String imeListe = getIntent().getStringExtra("listName");
        idOfList = getIntent().getIntExtra("idOfList", 0);
        listCode = getIntent().getStringExtra("listCode");

        TextView textView = findViewById(R.id.textView);
        textView.setText(imeListe);

        dodaj = findViewById(R.id.dodaj);
        prikaziKodListe = findViewById(R.id.prikaziKodListe);

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (SelectedList.this, AddItem.class);
                intent.putExtra("idOfList", idOfList);
                intent.putExtra("listName", imeListe);
                intent.putExtra("listCode", listCode);
                startActivity(intent);
            }
        });

        prikaziKodListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCodeDialog();
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

    public void dodajItem(View view) {
    }

    @Override
    public void onItemClick(int position) {

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
