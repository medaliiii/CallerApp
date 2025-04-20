package com.example.callerapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AvatarResponse {
    private String avatarUrl;

    public AvatarResponse(String seed) {
        this.avatarUrl = generateAvatarUrl(seed);
    }

    private String generateAvatarUrl(String seed) {
        try {
            // Encoder le seed pour l'URL
            String encodedSeed = URLEncoder.encode(seed, "UTF-8")
                    .replaceAll("\\+", "%20"); // Remplacer les '+' par '%20'

            return "https://api.dicebear.com/7.x/avataaars/png" +
                    "?seed=" + encodedSeed +
                    "&backgroundColor=e91e63" +
                    "&radius=50" +
                    "&size=100";
        } catch (UnsupportedEncodingException e) {
            // URL de fallback en cas d'erreur
            return "https://api.dicebear.com/7.x/avataaars/png?seed=default&backgroundColor=e91e63&radius=50&size=100";
        }
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}