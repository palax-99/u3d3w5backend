package antoninopalazzolo.u3d3w5backend.controllers;

import antoninopalazzolo.u3d3w5backend.entities.Event;
import antoninopalazzolo.u3d3w5backend.entities.User;
import antoninopalazzolo.u3d3w5backend.exceptions.ValidationException;
import antoninopalazzolo.u3d3w5backend.payloads.EventDTO;
import antoninopalazzolo.u3d3w5backend.services.EventsService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventsController {

    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    // tutti possono vedere gli eventi
    @GetMapping
    public Page<Event> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "date") String sortBy) {
        return this.eventsService.findAll(page, size, sortBy);
    }

    // tutti possono vedere un singolo evento
    @GetMapping("/{eventId}")
    public Event getEventById(@PathVariable UUID eventId) {
        return this.eventsService.findById(eventId);
    }

    // solo gli organizzatori possono creare eventi
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public Event createEvent(@RequestBody @Validated EventDTO body,
                             BindingResult validationResult,
                             @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }
        return this.eventsService.save(body, currentUser);
    }

    // solo l'organizzatore può modificare il proprio evento
    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public Event updateEvent(@PathVariable UUID eventId,
                             @RequestBody @Validated EventDTO body,
                             BindingResult validationResult,
                             @AuthenticationPrincipal User currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }
        return this.eventsService.findByIdAndUpdate(eventId, body, currentUser);
    }

    // solo l'organizzatore può eliminare il proprio evento
    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public void deleteEvent(@PathVariable UUID eventId,
                            @AuthenticationPrincipal User currentUser) {
        this.eventsService.findByIdAndDelete(eventId, currentUser);
    }
}
