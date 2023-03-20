package com.example.c3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public Button izaberi;
    public Button kreirajNovuListu;

    public Button pridruziSeListi;
    RecyclerView recyclerView;
    ArrayList<String> editList;
    ArrayList<Integer> idList;

    ArrayList<String> listCode;
    DBHelper DB;
    MyAdapter adapter;

    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        izaberi = findViewById(R.id.izaberi);
        kreirajNovuListu = findViewById(R.id.kreirajNovuListu);
        pridruziSeListi = findViewById(R.id.pridruziSeListi);

        userId = getIntent().getIntExtra("userId", 0);

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
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        pridruziSeListi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCodeDialog();
            }
        });

        DB = new DBHelper(this);
        editList = new ArrayList<>();
        idList = new ArrayList<>();
        listCode = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MyAdapter(this, editList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displaydata();
    }

    private void displaydata() {
        Cursor corsorUserList = DB.getAllLists("UserList");
        while(corsorUserList.moveToNext()) {
            if(userId == corsorUserList.getInt(1)) {
                Cursor cursor = DB.getAllLists("List");
                while (cursor.moveToNext()) {
                    if(corsorUserList.getInt(2) == cursor.getInt(0)) {
                        editList.add(cursor.getString(1));
                        idList.add(cursor.getInt(0));
                        listCode.add(cursor.getString(2));
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, SelectedList.class);

        intent.putExtra("listName", String.valueOf(editList.get(position)));
        intent.putExtra("idOfList", idList.get(position));
        intent.putExtra("listCode", listCode.get(position));
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        String ime = editList.get(position).toString();
        Boolean deleteData = DB.deleteList(ime);

        editList.remove(position);
        adapter.notifyItemRemoved(position);

    }

    public void createNewCodeDialog(){
        System.out.println("********************************************");
        dialogBuilder = new AlertDialog.Builder(this);
        final View codePopupView = getLayoutInflater().inflate(R.layout.inputcodepopup, null);

        EditText code = codePopupView.findViewById(R.id.codeListe);

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
                String codeListe = code.getText().toString();

                int listId = DB.checkCode(codeListe);

                if(listId != -1) {
                    Boolean checkInsertUserList = DB.insertUserList(userId, listId);
                    if(checkInsertUserList) {
                        editList.removeAll(editList);
                        idList.removeAll(idList);
                        listCode.removeAll(listCode);
                        displaydata();
                        dialog.dismiss();
                    }
                }
            }
        });
    }
}