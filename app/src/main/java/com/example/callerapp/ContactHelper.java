package com.example.callerapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ContactHelper extends SQLiteOpenHelper {
    public static final String table_contact="Contacts";
    public static final String col_id="ID";
    public static final String col_nom="nom";
    public static final String col_pseudo="pseudo";
    public static final String col_num="num";

    String requete="create table "+table_contact+"("+col_id+" Integer Primary Key Autoincrement," +  col_nom+" Text not null,"+col_pseudo+" Text not null,"+col_num+" Integer)";


    public ContactHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Lors de l'ouverture de la bdd
        db.execSQL(requete);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //en cas de modification de la version
        db.execSQL(" drop table "+table_contact);
        onCreate(db);
    }
}
