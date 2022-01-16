package com.example.dotaapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String TAG = "DATABASE_HELPER";

    private static final String TABLE_NAME = "user_table";
    private static final String COL1 = "username";
    private static final String COL2 = "location";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "( " + COL1 +
        " TEXT, " + COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String username, String location){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL1, username);
        cv.put(COL2, location);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            return false;}
        else{
            return true;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String name, String loc, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL1 + " = '" + name + "' , "
                + COL2 + " = '" + loc + "' WHERE " + COL1 + " = '" + oldName + "'";
        Log.i(TAG, "updateName: " + query);
        db.execSQL(query);
    }
}
