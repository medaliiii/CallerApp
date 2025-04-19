package com.example.callerapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Ajout extends AppCompatActivity {

    private static final String BASE_URL = "https://apilayer.net/api/";
    private static final String API_KEY = "93b297cb64771a5530e1225d0bf29c90";

    private Button btnsauvegard, btnquitter;
    private EditText ednom, edpseudo, ednum;
    private Spinner spinnerCountryCode;
    private ContactManager manager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout);

        initViews();
        manager = new ContactManager(this);
        manager.ouvrir();
        initRetrofit();
        setupListeners();
    }

    private void initViews() {
        ednom = findViewById(R.id.ednom_ajout);
        edpseudo = findViewById(R.id.edpseudo_ajout);
        ednum = findViewById(R.id.ednum_ajout);
        spinnerCountryCode = findViewById(R.id.spinner_country_code);
        btnquitter = findViewById(R.id.btnquitter_ajout);
        btnsauvegard = findViewById(R.id.btnsauvgarder_ajout);
    }

    private void initRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    private void setupListeners() {
        btnquitter.setOnClickListener(view -> finish());

        btnsauvegard.setOnClickListener(view -> {
            String nom = ednom.getText().toString().trim();
            String pseudo = edpseudo.getText().toString().trim();
            String num = ednum.getText().toString().trim();

            if (validateInputs(nom, pseudo, num)) {
                String countryCode = spinnerCountryCode.getSelectedItem().toString().trim();
                String fullNumber = countryCode + num;
                validatePhoneNumber(fullNumber, nom, pseudo);
            }
        });
    }

    private boolean validateInputs(String nom, String pseudo, String num) {
        if (nom.isEmpty() || pseudo.isEmpty() || num.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void validatePhoneNumber(String rawNumber, String nom, String pseudo) {
        final String fullNumber = formatPhoneNumber(rawNumber);

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Validation...");
        progress.setCancelable(false);
        progress.show();

        apiService.validateNumber(API_KEY, fullNumber).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progress.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isValid()) {
                        saveContact(nom, pseudo, fullNumber);
                    } else {
                        showValidationDialog(response.body(), nom, pseudo, fullNumber);
                    }
                } else {
                    saveContact(nom, pseudo, fullNumber); // Fallback
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                progress.dismiss();
                saveContact(nom, pseudo, fullNumber); // Fallback
            }
        });
    }

    private String formatPhoneNumber(String rawNumber) {
        String cleaned = rawNumber.replaceAll("[^0-9+]", "");
        return cleaned.startsWith("+") ? cleaned : "+" + cleaned;
    }

    private void handleApiResponse(Response<ApiResponse> response, String nom, String pseudo, String number) {
        if (response.isSuccessful() && response.body() != null) {
            ApiResponse apiResponse = response.body();
            Log.d("API_RESPONSE", new Gson().toJson(apiResponse));

            if (apiResponse.isValid()) {
                saveContact(nom, pseudo, number);
            } else {
                showValidationDialog(apiResponse, nom, pseudo, number);
            }
        } else {
            Toast.makeText(this, "Erreur API: " + response.message(), Toast.LENGTH_LONG).show();
            saveContact(nom, pseudo, number);
        }
    }

    private void handleNetworkError(Throwable t, String nom, String pseudo, String number) {
        Log.e("NETWORK_ERROR", "Erreur: ", t);
        Toast.makeText(this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
        saveContact(nom, pseudo, number);
    }

    private void showValidationDialog(ApiResponse response, String nom, String pseudo, String number) {
        String message = String.format(
                "Résultat API:\n\n" +
                        "Valid: %b\n" +
                        "Number: %s\n" +
                        "International: %s\n" +
                        "Country: %s\n" +
                        "Carrier: %s\n\n" +
                        "Enregistrer quand même?",
                response.isValid(),
                response.getNumber(),
                response.getInternationalFormat(),
                response.getCountryName(),
                response.getCarrier() != null ? response.getCarrier() : "Inconnu"
        );

        new AlertDialog.Builder(this)
                .setTitle("Détails API")
                .setMessage(message)
                .setPositiveButton("Oui", (d, i) -> saveContact(nom, pseudo, number))
                .setNegativeButton("Non", null)
                .show();
    }

    private void saveContact(String nom, String pseudo, String fullNumber) {
        if (nom.isEmpty() || pseudo.isEmpty() || fullNumber.isEmpty()) {
            Toast.makeText(this, "Erreur: Champs manquants", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = manager.ajout(nom, pseudo, fullNumber);

        if (result != -1) {
            Toast.makeText(this, "Contact enregistré", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Échec de l'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        runOnUiThread(() -> {
            ednom.setText("");
            edpseudo.setText("");
            ednum.setText("");
        });
    }

    public interface ApiService {
        @GET("validate")
        Call<ApiResponse> validateNumber(
                @Query("access_key") String apiKey,
                @Query("number") String phoneNumber
        );
    }
}