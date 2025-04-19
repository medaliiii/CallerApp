package com.example.callerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UserManager {

    private DatabaseHelper dbHelper;

    public UserManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void addUser(String name, String password, String phone) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_PHONE, phone);
        db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();
    }

}
