package com.example.callerapp;

import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("valid")
    private boolean valid;

    @SerializedName("number")
    private String number;

    @SerializedName("international_format")
    private String internationalFormat;

    @SerializedName("country_name")
    private String countryName;

    @SerializedName("carrier")
    private String carrier;

    // Getters
    public boolean isValid() { return valid; }
    public String getNumber() { return number; }
    public String getInternationalFormat() { return internationalFormat; }
    public String getCountryName() { return countryName; }
    public String getCarrier() { return carrier; }
}