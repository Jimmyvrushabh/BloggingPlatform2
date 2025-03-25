package com.Spring.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

@Service
public class GoogleAuthService {

    private static final String GOOGLE_CLIENT_ID = "490647164435-3hn4egh6fkdg1natanm509fru9r10pmn.apps.googleusercontent.com";

    public Map<String, String> verifyGoogleToken(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID)) // Set client ID
                    .build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extract user information
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("email", payload.getEmail()); // User email
                userInfo.put("name", (String) payload.get("name")); // User name
                
                return userInfo;
            } else {
                return null; // Invalid token
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Verification failed
        }
    }
}
