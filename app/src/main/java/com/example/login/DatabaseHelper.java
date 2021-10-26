package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MiniProject";
    private static final String TABLE_NAME = "Login";
    private static final String COLUMN_1 = "Username";
    private static final String COLUMN_2 = "Password";
    private static final String COLUMN_3 = "Contact_no";

    public DatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME + "(Username VARCHAR PRIMARY KEY, Password VARCHAR, Contact_no VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean registerUser(String Username, String Password, String Contact_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_1, Username);
        values.put(COLUMN_2, Password);
        values.put(COLUMN_3, Contact_no);

        long result = db.insert(TABLE_NAME, null, values);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public boolean checkUser(String Username, String Password){
        SQLiteDatabase db = this.getWritableDatabase();
        String [] columns = {COLUMN_1};
        String selection = COLUMN_1 + "=?" + " and " + COLUMN_2 + "=?";
        String [] selectionargs = {Username, Password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionargs, null, null, null );
        int count = cursor.getCount();
        db.close();
        cursor.close();
        if(count > 0){
            return true;
        } else {
            return false;
        }
    }

    public void update(String Username, String UpdatedUsername, String Password, String Contact_no){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("Update " +TABLE_NAME+ " set Username='" + UpdatedUsername + "' AND Password = '" +Password+ "' AND Contact_no = '" +Contact_no + "' where Username = '" +Username+ "'");
        db.close();
    }
}

