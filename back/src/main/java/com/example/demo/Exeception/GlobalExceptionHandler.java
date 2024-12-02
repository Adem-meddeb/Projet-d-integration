package com.example.demo.Exeception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

// La classe annotée avec @ControllerAdvice permet de définir une gestion centralisée des exceptions
@ControllerAdvice
public class GlobalExceptionHandler {

    // Gestion des exceptions spécifiques de type ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        // Création d'un corps de réponse sous forme d'une Map pour un retour JSON structuré
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getReason()); // Récupère la raison de l'exception (ex. : "Ressource introuvable")
        body.put("status", ex.getStatusCode().value()); // Ajoute le code HTTP de l'exception (ex. : 404)

        // Retourne une réponse HTTP contenant le corps JSON et le code HTTP de l'exception
        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    // Gestion générale pour toutes les autres exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        // Création d'un corps de réponse générique
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Une erreur interne s'est produite"); // Message d'erreur générique
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // Code HTTP 500 pour erreur interne

        // Retourne une réponse HTTP avec le corps JSON et un statut 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
