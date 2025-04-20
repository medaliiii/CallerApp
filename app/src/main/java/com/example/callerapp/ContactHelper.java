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
    public static final String col_avatar = "avatar_url";

    String requete = "create table " + table_contact + "("
            + col_id + " Integer Primary Key Autoincrement,"
            + col_nom + " Text not null,"
            + col_pseudo + " Text not null,"
            + col_num + " Text,"
            + col_avatar + " Text)";


    public ContactHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, 3); // Passez à la version 3
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
