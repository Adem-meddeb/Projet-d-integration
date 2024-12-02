package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Désactivation de la protection CSRF (Cross-Site Request Forgery)
            .authorizeHttpRequests() // Commence la configuration des autorisations des requêtes HTTP
            .requestMatchers("**").permitAll() // Permet à toutes les requêtes d'accéder sans authentification
            .and()
            .httpBasic(); // Active l'authentification HTTP Basic

        return http.build(); // Construit et retourne la chaîne de filtres de sécurité
    }
}