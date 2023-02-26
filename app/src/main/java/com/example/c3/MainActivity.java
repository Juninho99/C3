package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{

    public Button izaberi;
    public Button kreirajNovuListu;
    RecyclerView recyclerView;
    ArrayList<String> editList;
    ArrayList<Integer> idList;
    DBHelper DB;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        izaberi = findViewById(R.id.izaberi);
        kreirajNovuListu = findViewById(R.id.kreirajNovuListu);

        izaberi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, SelectedList.class);
                startActivity(intent);
            }
        });

        kreirajNovuListu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, AddList.class);
                startActivity(intent);
            }
        });

        DB = new DBHelper(this);
        editList = new ArrayList<>();
        idList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(this, editList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();
    }

    private void displaydata() {
        Cursor cursor = DB.getAllLists("List");
        while(cursor.moveToNext())
        {
            editList.add(cursor.getString(1));
            idList.add(cursor.getInt(0));
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, SelectedList.class);

        intent.putExtra("listName", String.valueOf(editList.get(position)));
        intent.putExtra("idOfList", idList.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        String ime = editList.get(position).toString();
        Boolean deleteData = DB.deleteList(ime);

        editList.remove(position);
        adapter.notifyItemRemoved(position);

    }
}