package com.example.demo.services;

import java.util.List;

import com.example.demo.Exeception.ResourceNotFoundException;
import com.example.demo.Exeception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.EventDTO;
import com.example.demo.Entité.Event;
import com.example.demo.Entité.User;
import com.example.demo.Repo.EventRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        System.out.println("Number of events: " + events.size());
        return events;  // Retourner la liste des événements
    }

    public EventDTO createEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        
        // Créer un DTO pour renvoyer les informations de l'événement sauvegardé
        EventDTO eventDTO = new EventDTO();
        eventDTO.setId(savedEvent.getId());
        eventDTO.setTitle(savedEvent.getTitle());
        eventDTO.setCategory(savedEvent.getCategory());
        eventDTO.setLocation(savedEvent.getLocation());
        eventDTO.setDate(savedEvent.getDate());
        
        // Retourner le DTO de l'événement
        return eventDTO;
    }

    public List<Event> searchEvents(String category, String title) {
        return eventRepository.findByCategoryContainingOrTitleContaining(category, title);
    }

    public void registerForEvent(Long eventId, User user) {
        // Rechercher l'événement par son ID
        Event event = eventRepository.findById(eventId)
                                     .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        
        // Ajouter l'utilisateur à la liste des participants de l'événement
        event.getParticipants().add(user);
        // Ajouter l'événement à la liste des événements enregistrés de l'utilisateur
        user.getRegisteredEvents().add(event);
        
        // Sauvegarder l'événement avec les modifications
        eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long eventId, EventDTO eventDTO, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        if (!event.getOrganizer().getId().equals(userId)) {
            // Si l'utilisateur n'est pas l'organisateur, lancer une exception UnauthorizedException
            throw new UnauthorizedException("You are not authorized to update this event");
        }

        // Mettre à jour les champs de l'événement avec les données du DTO
        event.setTitle(eventDTO.getTitle());
        event.setDate(eventDTO.getDate());
        event.setLocation(eventDTO.getLocation());
        event.setCategory(eventDTO.getCategory());
        event.setImageUrl(eventDTO.getImageUrl());

        // Sauvegarder l'événement mis à jour
        return eventRepository.save(event);
    }

    public void deleteEvent(Long eventId, Long organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Événement introuvable"));

        // Vérification : Seul l'organisateur peut supprimer l'événement
        if (!event.getOrganizer().getId().equals(organizerId)) {
            // Si l'utilisateur n'est pas l'organisateur, lancer une exception RuntimeException
            throw new RuntimeException("Vous n'êtes pas autorisé à supprimer cet événement.");
        }

        eventRepository.delete(event);
    }
}