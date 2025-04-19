package com.example.callerapp;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ContactManager {
    SQLiteDatabase db;
    Context context;

    public ContactManager(Context context) {
        this.context = context;
    }

    public void ouvrir() {
        ContactHelper helper = new ContactHelper(context, "mabase.db", null, 2);
        db = helper.getWritableDatabase();
    }

    public long ajout(String nom, String pseudo, String num) {
        ContentValues values = new ContentValues();
        values.put(ContactHelper.col_nom, nom);
        values.put(ContactHelper.col_pseudo, pseudo);
        values.put(ContactHelper.col_num, num);
        return db.insert(ContactHelper.table_contact, null, values);
    }

    public ArrayList<Contact> getAllContact() {
        ArrayList<Contact> listContacts = new ArrayList<>();

        Cursor cursor = db.query(ContactHelper.table_contact,
                new String[]{ContactHelper.col_id, ContactHelper.col_nom, ContactHelper.col_pseudo, ContactHelper.col_num},
                null, null, null, null, ContactHelper.col_nom + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(ContactHelper.col_id));
                String nom = cursor.getString(cursor.getColumnIndex(ContactHelper.col_nom));
                String pseudo = cursor.getString(cursor.getColumnIndex(ContactHelper.col_pseudo));
                String num = cursor.getString(cursor.getColumnIndex(ContactHelper.col_num));

                Contact contact = new Contact(id, nom, pseudo, num);
                listContacts.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return listContacts;
    }

    public void supprimerContact(int id) {
        db.delete(ContactHelper.table_contact, ContactHelper.col_id + " = ?", new String[]{String.valueOf(id)});
    }

    public int modifierContact(int id, String nom, String pseudo, String num) {
        ContentValues values = new ContentValues();
        values.put(ContactHelper.col_nom, nom);
        values.put(ContactHelper.col_pseudo, pseudo);
        values.put(ContactHelper.col_num, num);

        return db.update(ContactHelper.table_contact, values, ContactHelper.col_id + " = ?", new String[]{String.valueOf(id)});
    }

    public void fermer() {
        db.close();
    }
}

