package com.example.c3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "List.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE List(id INTEGER PRIMARY KEY, name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("DROP TABLE IF EXISTS List");
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
        long result = DB.delete("List", "name=?", new String[]{name});
        return result != -1;
    }

    public Cursor getAllLists() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM List", null);
        return cursor;
    }
}
