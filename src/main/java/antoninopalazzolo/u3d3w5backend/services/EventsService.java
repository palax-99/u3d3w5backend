package antoninopalazzolo.u3d3w5backend.services;

import antoninopalazzolo.u3d3w5backend.entities.Event;
import antoninopalazzolo.u3d3w5backend.entities.User;
import antoninopalazzolo.u3d3w5backend.exceptions.BadRequestException;
import antoninopalazzolo.u3d3w5backend.exceptions.NotFoundException;
import antoninopalazzolo.u3d3w5backend.payloads.EventDTO;
import antoninopalazzolo.u3d3w5backend.repositories.EventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EventsService {

    private final EventsRepository eventsRepository;

    public EventsService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    // tutti possono vedere gli eventi
    public Page<Event> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventsRepository.findAll(pageable);
    }

    public Event findById(UUID eventId) {
        return this.eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));
    }

    // solo l'organizzatore può creare eventi — l'utente autenticato viene passato dal controller
    public Event save(EventDTO body, User organizer) {
        Event newEvent = new Event(body.title(), body.description(), body.date(), body.location(), body.availableSeats(), organizer);
        Event savedEvent = this.eventsRepository.save(newEvent);
        log.info("Evento con id " + savedEvent.getId() + " salvato correttamente!");
        return savedEvent;
    }

    // solo l'organizzatore può modificare il proprio evento
    public Event findByIdAndUpdate(UUID eventId, EventDTO body, User currentUser) {
        Event found = this.findById(eventId);

        // controllo che chi sta modificando sia l'organizzatore dell'evento
        if (!found.getOrganizer().getId().equals(currentUser.getId()))
            throw new BadRequestException("Non puoi modificare un evento che non hai creato tu!");

        found.setTitle(body.title());
        found.setDescription(body.description());
        found.setDate(body.date());
        found.setLocation(body.location());
        found.setAvailableSeats(body.availableSeats());

        return this.eventsRepository.save(found);
    }

    // solo l'organizzatore può eliminare il proprio evento
    public void findByIdAndDelete(UUID eventId, User currentUser) {
        Event found = this.findById(eventId);

        if (!found.getOrganizer().getId().equals(currentUser.getId()))
            throw new BadRequestException("Non puoi eliminare un evento che non hai creato tu!");

        this.eventsRepository.delete(found);
    }

    //prof, service ultra difficile ahah
}
