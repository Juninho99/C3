package com.example.c3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "C3.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE List(id INTEGER PRIMARY KEY, name TEXT, code TEXT)");
        DB.execSQL("CREATE TABLE Article(id INTEGER PRIMARY KEY, name TEXT, quantity INTEGER, description TEXT, list_id INTEGER REFERENCES List(id))");
        DB.execSQL("CREATE TABLE User(id INTEGER PRIMARY KEY, username TEXT, name TEXT, surname TEXT, password TEXT)");
        DB.execSQL("CREATE TABLE UserList(id INTEGER PRIMARY KEY, idUser INTEGER REFERENCES User(id), idList INTEGER REFERENCES List(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("DROP TABLE IF EXISTS List");
        DB.execSQL("DROP TABLE IF EXISTS Article");
        DB.execSQL("DROP TABLE IF EXISTS User");
        DB.execSQL("DROP TABLE IF EXISTS UserList");
    }

    public boolean insertItem(String imeArtikla, String kolicina, String dodatneInfo, Integer idListe) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", imeArtikla);
        contentValues.put("quantity", kolicina);
        contentValues.put("description", dodatneInfo);
        contentValues.put("list_id", idListe);

        long result = DB.insert("Article", null, contentValues);

        return result != -1;
    }

    public boolean insertList(String name, String code) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("code", code);

        long result = DB.insert("List", null, contentValues);

        return result != -1;
    }

    public boolean insertUser(String username, String name, String surname, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("password", password);

        long result = DB.insert("User", null, contentValues);

        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM User", null);
        while(cursor.moveToNext())
        {
            if(cursor.getString(1).equals(username) && cursor.getString(4).equals(password))
                return true;
        }
        return false;
    }

    public boolean deleteList(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String deleteTable = "DROP TABLE IF EXISTS " + name;
        DB.execSQL(deleteTable);
        long result = DB.delete("List", "name=?", new String[]{name});
        return result != -1;
    }

    public Cursor getAllLists(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM " + name, null);
        return cursor;
    }
}
