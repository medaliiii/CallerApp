package com.example.callerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Affichage extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 1001;
    private static final int CALL_PHONE_REQUEST_CODE = 1;

    private RecyclerView rv;
    private MyContactRecyclerAdapter adapter;
    private ArrayList<Contact> originalContacts;
    private ArrayList<Contact> filteredContacts;
    private ContactManager manager;
    private String phoneNumberToCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage);

        rv = findViewById(R.id.rvaffiche);
        manager = new ContactManager(this);
        manager.ouvrir();

        originalContacts = manager.getAllContact();
        filteredContacts = new ArrayList<>(originalContacts);

        adapter = new MyContactRecyclerAdapter(this, filteredContacts);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);
                return true;
            }
        });
    }

    private void filterContacts(String query) {
        filteredContacts.clear();

        if (query.isEmpty()) {
            filteredContacts.addAll(originalContacts);
        } else {
            String searchText = query.toLowerCase();
            for (Contact contact : originalContacts) {
                if (contact.getNom().toLowerCase().contains(searchText) ||
                        contact.getPseudo().toLowerCase().contains(searchText) ||
                        contact.getNum().contains(query)) {
                    filteredContacts.add(contact);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

   /* public void makePhoneCall(String phoneNumber) {
        phoneNumberToCall = phoneNumber;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_REQUEST_CODE);
        } else {
            startCall(phoneNumber);
        }
    }*/

    private void startCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (adapter != null) {
                adapter.handleGalleryResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CALL_PHONE_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (phoneNumberToCall != null) {
                        startCall(phoneNumberToCall);
                    }
                } else {
                    Toast.makeText(this,
                            "Permission d'appel refusée",
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case GALLERY_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (adapter != null) {
                        adapter.openGalleryForCurrentContact();
                    }
                } else {
                    Toast.makeText(this,
                            "Permission galerie refusée",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        manager.fermer();
        super.onDestroy();
    }
}