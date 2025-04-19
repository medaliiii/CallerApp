package com.example.callerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText edNom, edMotDePasse;
    private Button btnConnexion;
    private Button btnQuitter;
    private CheckBox checkBoxAuth;
    private TextView tvCreerCompte;

    SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "remember";

    DatabaseHelper dbHelper; // Classe pour gérer la base de données

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNom = findViewById(R.id.ed_nom_auth);
        edMotDePasse = findViewById(R.id.ed_pw_auth);
        btnConnexion = findViewById(R.id.btn_con_auth);
        btnQuitter = findViewById(R.id.btn_qtt_auth);
        checkBoxAuth = findViewById(R.id.checkBoxauth);
        tvCreerCompte = findViewById(R.id.tvAdduser_auth);

        // Initialisation des SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Vérifier si l'utilisateur est déjà connecté
        if (sharedPreferences.getBoolean(KEY_REMEMBER, false)) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            edNom.setText(savedUsername);
            edMotDePasse.setText(savedPassword);
            checkBoxAuth.setChecked(true);

            // Passer directement à l'activité suivante
            Intent i = new Intent(MainActivity.this, Accueil.class);
            i.putExtra("USER", savedUsername);
            startActivity(i);
            finish();
        }

        // Initialiser la base de données
        dbHelper = new DatabaseHelper(this);

        // Gérer le clic sur le bouton "Créer un compte"
        tvCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Gérer le clic sur le bouton "Quitter"
        btnQuitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });

        // Gérer le clic sur le bouton "Se connecter"
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom = edNom.getText().toString().trim();
                String mp = edMotDePasse.getText().toString().trim();

                if (nom.isEmpty() || mp.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                } else {
                    // Vérifier les informations d'identification dans la base de données
                    if (validateUser(nom, mp)) {
                        if (checkBoxAuth.isChecked()) {
                            // Sauvegarder les informations de connexion
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_USERNAME, nom);
                            editor.putString(KEY_PASSWORD, mp);
                            editor.putBoolean(KEY_REMEMBER, true);
                            editor.apply();
                        } else {
                            // Effacer les informations si l'utilisateur ne veut pas rester connecté
                            sharedPreferences.edit().clear().apply();
                        }

                        Intent i = new Intent(MainActivity.this, Accueil.class);
                        i.putExtra("USER", nom);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Nom ou mot de passe non valide !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    // Méthode pour valider l'utilisateur
    private boolean validateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE name=? AND password=?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
}
