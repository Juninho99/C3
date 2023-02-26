package com.example.c3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Article.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE List(id INTEGER PRIMARY KEY, name TEXT)");
        DB.execSQL("CREATE TABLE Article(id INTEGER PRIMARY KEY, name TEXT, quantity INTEGER, description TEXT, list_id INTEGER REFERENCES List(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("DROP TABLE IF EXISTS List");
        DB.execSQL("DROP TABLE IF EXISTS Article");
    }

    public void makeNewTable(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL(name);
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

    public boolean insertList(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);

        long result = DB.insert("List", null, contentValues);

        return result != -1;
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
