package com.example.demo.Config;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtTokenUtil {

    /**
     * Génère un jeton simple encodé en Base64 à partir d'une adresse email et de l'horodatage actuel.
     * 
     * @param email L'adresse email utilisée comme partie du jeton.
     * @return Une chaîne de caractères encodée en Base64 représentant le jeton.
     */
    public static String generateToken(String email) {
        String payload = email + ":" + System.currentTimeMillis();
        // Le payload est encodé en Base64 pour générer un jeton
        return Base64.getEncoder().encodeToString(payload.getBytes());
    }

    /**
     * Décodage d'un jeton encodé en Base64 pour obtenir le payload original.
     * 
     * @param token Le jeton encodé en Base64.
     * @return Une chaîne de caractères contenant le payload décodé.
     */
    public static String decodeToken(String token) {
        // Décodage du jeton en Base64
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        // Conversion des octets en chaîne de caractères
        return new String(decodedBytes);
    }
}
