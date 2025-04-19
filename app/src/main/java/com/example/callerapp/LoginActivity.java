package com.example.callerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edNom, edMotDePasse, edNumTel;
    private Button btnCreerCompte;
    private TextView con; // Déclaration du TextView
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edNom = findViewById(R.id.ed_nom);
        edMotDePasse = findViewById(R.id.ed_motdepasse);
        edNumTel = findViewById(R.id.ed_numtel);
        btnCreerCompte = findViewById(R.id.btn_creer_compte);
        con = findViewById(R.id.con); // Initialisation du TextView

        userManager = new UserManager(this);

        btnCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = edNom.getText().toString();
                String motDePasse = edMotDePasse.getText().toString();
                String numTel = edNumTel.getText().toString();

                if (!nom.isEmpty() && !motDePasse.isEmpty() && !numTel.isEmpty()) {
                    userManager.addUser(nom, motDePasse, numTel);
                    Toast.makeText(LoginActivity.this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Ajout du OnClickListener pour le TextView con
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent pour naviguer vers MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
