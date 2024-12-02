package com.example.demo.Controller;

import com.example.demo.Repo.EventRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Config.JwtTokenUtil;
import com.example.demo.Dto.EventDTO;
import com.example.demo.Entité.Event;
import com.example.demo.Entité.User;
import com.example.demo.Repo.UserRepository;
import com.example.demo.services.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents(@RequestHeader("Authorization") String authHeader) {
        System.out.println("test  afficher tous");

        // Décoder le token JWT pour récupérer l'email de l'utilisateur
        String token = authHeader.replace("Bearer ", "");
        String email = JwtTokenUtil.decodeToken(token).split(":")[0];

        // Récupérer l'utilisateur à partir de l'email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Récupérer la liste de tous les événements
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody Event event, @RequestHeader("Authorization") String authHeader) {
        System.out.println("test create");

        // Extraire le token JWT et l'email de l'utilisateur
        String token = authHeader.replace("Bearer ", "");
        String email = (JwtTokenUtil.decodeToken(token)).split(":")[0];

        // Récupérer l'utilisateur correspondant à l'email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Définir l'utilisateur comme organisateur de l'événement
        event.setOrganizer(user);

        // Créer l'événement via le service
        EventDTO savedEventDto = eventService.createEvent(event);

        // Retourner le DTO de l'événement créé
        return ResponseEntity.ok(savedEventDto);
    }

    @GetMapping
    public ResponseEntity<List<Event>> searchEvents(@RequestParam(required = false) String category,
                                                    @RequestParam(required = false) String title,
                                                    @RequestHeader("Authorization") String authHeader) {
        System.out.println("test ll search");

        // Décoder le token pour obtenir l'email de l'utilisateur
        String token = authHeader.replace("Bearer ", "");
        String email = JwtTokenUtil.decodeToken(token).split(":")[0];

        // Trouver l'utilisateur par son email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Rechercher des événements en fonction des paramètres de recherche
        List<Event> events = eventService.searchEvents(category, title);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/{eventId}/register")
    public ResponseEntity<Void> registerForEvent(@PathVariable Long eventId,
                                                 @RequestHeader("Authorization") String authHeader) {
        System.out.println("test registration f event");

        // Extraire le token et l'email de l'utilisateur
        String token = authHeader.replace("Bearer ", "");
        String email = JwtTokenUtil.decodeToken(token).split(":")[0];

        // Récupérer l'utilisateur par son email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Inscrire l'utilisateur à l'événement
        eventService.registerForEvent(eventId, user);
        return ResponseEntity.ok().build();  // Retourner une réponse vide avec un statut 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @RequestBody @Valid EventDTO eventDTO,
            @RequestHeader("Authorization") String authHeader
    ) {
        // Extraire l'email de l'utilisateur depuis le token JWT
        String token = authHeader.replace("Bearer ", "");
        String email = JwtTokenUtil.decodeToken(token).split(":")[0];

        // Trouver l'utilisateur correspondant à l'email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Mettre à jour l'événement via le service
        Event updatedEvent = eventService.updateEvent(id, eventDTO, user.getId());
        return ResponseEntity.ok(updatedEvent);  // Retourner l'événement mis à jour
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        // Vérifier si l'événement existe dans la base de données
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();  // Retourner une réponse 404 si l'événement n'est pas trouvé
        }

        // Supprimer les utilisateurs inscrits à l'événement
        userRepository.deleteByRegisteredEventsId(id);  // Utiliser la méthode dans UserRepository pour supprimer les inscriptions

        // Supprimer l'événement
        eventRepository.deleteById(id);

        return ResponseEntity.noContent().build();  // Retourner une réponse 204 No Content après la suppression
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Event>> getEvent(@PathVariable Long id, @RequestHeader("Authorization") String authHeader) {
        // Vérifier la présence de l'en-tête Authorization et si le token commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Retourner un statut 401 Unauthorized si le token est invalide
        }

        // Extraire l'email de l'utilisateur à partir du token
        String token = authHeader.replace("Bearer ", "");
        String email = JwtTokenUtil.decodeToken(token).split(":")[0];

        // Récupérer l'utilisateur en fonction de l'email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Récupérer l'événement par son ID
        Optional<Event> event = eventRepository.findById(id);
        return ResponseEntity.ok(event);  // Retourner l'événement avec un statut 200 OK
    }
}
