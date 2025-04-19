package com.example.callerapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Accueil extends AppCompatActivity {

    private TextView tvusername;
    private Button btnajout, btnaff, btndeconnecter;
    public static ArrayList<Contact> contacts = new ArrayList<>();

    // Notification variables
    private static final String CHANNEL_ID = "caller_app_channel";
    private int notificationId = 0;

    // Permission handling
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this,
                            "Les notifications sont désactivées. Certaines fonctionnalités peuvent ne pas fonctionner.",
                            Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        // Initialize views
        tvusername = findViewById(R.id.tvuser_acc);
        btnajout = findViewById(R.id.btn_ajout_acc);
        btnaff = findViewById(R.id.btn_afficher_acc);
        btndeconnecter = findViewById(R.id.btn_deconnecter);

        // Setup notification channel
        createNotificationChannel();

        // Get username from intent
        Intent x = this.getIntent();
        Bundle b = x.getExtras();
        String u = b.getString("USER");
        tvusername.setText("Accueil de " + u);

        // Setup button listeners
        setupButtonListeners();

        // Show welcome notification after 5 seconds
        new Handler(Looper.getMainLooper()).postDelayed(this::showWelcomeNotification, 5000);
    }

    private void setupButtonListeners() {
        btnajout.setOnClickListener(view -> {
            Intent i = new Intent(Accueil.this, Ajout.class);
            startActivity(i);
            showActionNotification("Ajout de contact", "Prêt à ajouter un nouveau contact");
        });

        btnaff.setOnClickListener(view -> {
            Intent i = new Intent(Accueil.this, Affichage.class);
            startActivity(i);
            showActionNotification("Affichage", "Consultation de votre liste de contacts");
        });

        btndeconnecter.setOnClickListener(view -> {
            // Clear login information
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();

            // Show logout notification
            showLogoutNotification();

            // Return to login screen
            Intent i = new Intent(Accueil.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Caller App Notifications";
            String description = "Notifications pour l'application Caller App";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showWelcomeNotification() {
        if (checkNotificationPermission()) {
            Intent intent = new Intent(this, Accueil.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Bienvenue dans CallerApp")
                    .setContentText("Commencez à gérer vos contacts maintenant!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(this).notify(notificationId++, builder.build());
        }
    }

    private void showActionNotification(String title, String message) {
        if (checkNotificationPermission()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(this).notify(notificationId++, builder.build());
        }
    }

    private void showLogoutNotification() {
        if (checkNotificationPermission()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("Déconnexion réussie")
                    .setContentText("Vous avez été déconnecté avec succès")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(this).notify(notificationId++, builder.build());
        }
    }

    private boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                return false;
            }
        }
        return true;
    }
}