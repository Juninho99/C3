package com.example.c3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SelectedList extends Activity implements RecyclerViewInterface{

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    String listCode;
    Button dodaj, prikaziKodListe, nazad, help;
    RecyclerView recyclerView;
    ArrayList<String> editList, opisList;
    ArrayList<Integer> kolList, idItem;
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
        help = findViewById(R.id.help);

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

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createHelpDialog();
            }
        });

        DB = new DBHelper(this);
        editList = new ArrayList<>();
        kolList = new ArrayList<>();
        opisList = new ArrayList<>();
        idItem = new ArrayList<>();
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
            if(cursor.getInt(4) == idOfList) {
                idItem.add(cursor.getInt(0));
                editList.add(cursor.getString(1));
                kolList.add(cursor.getInt(2));
                opisList.add(cursor.getString(3));
            }
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
        intent.putExtra("kolicina", kolList.get(position));
        intent.putExtra("opis", String.valueOf(opisList.get(position)));
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        createDeleteItemDialog(position);
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

    public void createDeleteItemDialog(int position){
        dialogBuilder = new AlertDialog.Builder(this);
        final View codePopupView = getLayoutInflater().inflate(R.layout.delete_item, null);

        Button ok = codePopupView.findViewById(R.id.ok);
        Button nazad = codePopupView.findViewById(R.id.nazad);

        dialogBuilder.setView(codePopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        nazad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idItema = idItem.get(position);
                Boolean deleteData = DB.deleteItem(idItema);

                editList.remove(position);
                adapter.notifyItemRemoved(position);

                Toast toast = Toast.makeText(getApplicationContext(), "Uspješno brisanje artikla", Toast.LENGTH_SHORT);
                toast.show();

                dialog.dismiss();
            }
        });
    }

    public void createHelpDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View codePopupView = getLayoutInflater().inflate(R.layout.help_activity_main, null);

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
