package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Config.JwtTokenUtil;
import com.example.demo.Entité.User;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

       @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        // Appelle le service pour enregistrer l'utilisateur et retourne la réponse.
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        // Authentification de l'utilisateur en utilisant l'email et le mot de passe fournis
        User user = userService.authenticate(credentials.get("email"), credentials.get("password"));
        
        // Génère un token JWT en utilisant l'email de l'utilisateur
        String token = JwtTokenUtil.generateToken(user.getEmail());

        // Crée une réponse contenant le token et les détails de l'utilisateur
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        
        // Retourne la réponse avec le statut HTTP 200 OK
        return ResponseEntity.ok(response);
    }
}