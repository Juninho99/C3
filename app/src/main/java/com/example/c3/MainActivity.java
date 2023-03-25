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
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RecyclerViewInterface{

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    public Button kreirajNovuListu;
    public Button pridruziSeListi;

    public Button profil, odjava, help;
    RecyclerView recyclerView;
    ArrayList<String> editList;
    ArrayList<Integer> idList;

    ArrayList<String> listCode;
    DBHelper DB;
    MyAdapter adapter;

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
        setContentView(R.layout.activity_main);

        kreirajNovuListu = findViewById(R.id.kreirajNovuListu);
        pridruziSeListi = findViewById(R.id.pridruziSeListi);
        profil = findViewById(R.id.profil);
        odjava = findViewById(R.id.odjava);
        help = findViewById(R.id.help);

        userId = getIntent().getIntExtra("userId", 0);

        kreirajNovuListu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddListDialog();
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, Profil.class);
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

        odjava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOdjavaDialog();
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
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        createDeleteListDialog(position);
    }

    @Override
    public void onBackPressed() {
        //ovo treba biti prazno, jer ako nije nadređena metoda odluči da se vrati na prethodni ekran, a ja ne želim to
    }

    public void createNewCodeDialog(){
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
                    Boolean temp = false;
                    Cursor corsorUserList = DB.getAllLists("UserList");
                    while(corsorUserList.moveToNext()) {
                        if(userId == corsorUserList.getInt(1) && corsorUserList.getInt(2) == listId) {
                            temp = true;
                            Toast toast = Toast.makeText(getApplicationContext(), "Već ste pridruženi ovoj listi.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }

                    if(!temp) {
                        Boolean checkInsertUserList = DB.insertUserList(userId, listId);
                        if (checkInsertUserList) {
                            editList.removeAll(editList);
                            idList.removeAll(idList);
                            listCode.removeAll(listCode);
                            recyclerView.setAdapter(adapter);
                            displaydata();
                            dialog.dismiss();

                            Toast toast = Toast.makeText(getApplicationContext(), "Uspješno pridruživanje listi", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Kod liste koji ste unijeli nije ispravan. Pokušajte ponovo.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void createOdjavaDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View codePopupView = getLayoutInflater().inflate(R.layout.odjava, null);

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
                Intent intent = new Intent (MainActivity.this, Login.class);
                startActivity(intent);
                Toast toast = Toast.makeText(getApplicationContext(), "Odjava je uspješna.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void createDeleteListDialog(int position){
        dialogBuilder = new AlertDialog.Builder(this);
        final View codePopupView = getLayoutInflater().inflate(R.layout.delete_list, null);

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
                int idListe = idList.get(position);
                Boolean deleteData = DB.deleteList(idListe, userId);

                editList.remove(position);
                adapter.notifyItemRemoved(position);

                Toast toast = Toast.makeText(getApplicationContext(), "Uspješno brisanje liste", Toast.LENGTH_SHORT);
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

    public void createAddListDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View codePopupView = getLayoutInflater().inflate(R.layout.addlist, null);

        EditText imeListe = codePopupView.findViewById(R.id.imeListe);

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
                String nameListe = imeListe.getText().toString();

                int listId = DB.checkName(nameListe);
                if(!nameListe.isEmpty()) {
                    if (listId != -1) {
                        Boolean temp = false;
                        Cursor corsorUserList = DB.getAllLists("UserList");
                        while (corsorUserList.moveToNext()) {
                            if (userId == corsorUserList.getInt(1) && corsorUserList.getInt(2) == listId) {
                                temp = true;
                                Toast toast = Toast.makeText(getApplicationContext(), "Već ste pridruženi ovoj listi.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        if (!temp) {
                            Boolean checkInsertUserList = DB.insertUserList(userId, listId);
                            if (checkInsertUserList) {
                                editList.removeAll(editList);
                                idList.removeAll(idList);
                                listCode.removeAll(listCode);
                                recyclerView.setAdapter(adapter);
                                displaydata();
                                dialog.dismiss();

                                Toast toast = Toast.makeText(getApplicationContext(), "Uspješno pridruživanje već postojećoj listi", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    } else {
                        Boolean checkInsertData = DB.insertList(nameListe, generateRandomCode());

                        if (checkInsertData) {
                            int listiId = DB.getListId(nameListe);
                            Boolean checkInsertUserList = DB.insertUserList(userId, listiId);
                            if (checkInsertUserList) {
                                editList.removeAll(editList);
                                idList.removeAll(idList);
                                listCode.removeAll(listCode);
                                recyclerView.setAdapter(adapter);
                                displaydata();
                                dialog.dismiss();

                                Toast toast = Toast.makeText(getApplicationContext(), "Uspješno kreiranje liste", Toast.LENGTH_SHORT);
                                toast.show();
                            }
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