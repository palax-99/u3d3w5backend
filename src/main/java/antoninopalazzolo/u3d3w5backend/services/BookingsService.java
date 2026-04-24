package antoninopalazzolo.u3d3w5backend.services;

import antoninopalazzolo.u3d3w5backend.entities.Booking;
import antoninopalazzolo.u3d3w5backend.entities.Event;
import antoninopalazzolo.u3d3w5backend.entities.User;
import antoninopalazzolo.u3d3w5backend.exceptions.BadRequestException;
import antoninopalazzolo.u3d3w5backend.exceptions.NotFoundException;
import antoninopalazzolo.u3d3w5backend.repositories.BookingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class BookingsService {

    private final BookingsRepository bookingsRepository;
    private final EventsService eventsService;

    public BookingsService(BookingsRepository bookingsRepository, EventsService eventsService) {
        this.bookingsRepository = bookingsRepository;
        this.eventsService = eventsService;
    }

    // l'utente prenota un posto per un evento
    public Booking save(UUID eventId, User currentUser) {
        Event event = this.eventsService.findById(eventId);

        // controllo che ci siano posti disponibili
        if (event.getAvailableSeats() <= 0)
            throw new BadRequestException("Non ci sono posti disponibili per questo evento!");

        // scalo un posto disponibile
        event.setAvailableSeats(event.getAvailableSeats() - 1);

        Booking newBooking = new Booking(currentUser, event);
        Booking savedBooking = this.bookingsRepository.save(newBooking);

        log.info("Prenotazione con id " + savedBooking.getId() + " salvata correttamente!");
        return savedBooking;
    }

    public Booking findById(UUID bookingId) {
        return this.bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(bookingId));
    }

    // l'utente può cancellare la propria prenotazione
    public void findByIdAndDelete(UUID bookingId, User currentUser) {
        Booking found = this.findById(bookingId);

        // controllo che chi cancella sia il proprietario della prenotazione
        if (!found.getUser().getId().equals(currentUser.getId()))
            throw new BadRequestException("Non puoi cancellare una prenotazione che non è tua!!!!");

        // restituisco il posto all'evento
        found.getEvent().setAvailableSeats(found.getEvent().getAvailableSeats() + 1);

        this.bookingsRepository.delete(found);
        log.info("Prenotazione con id " + bookingId + " cancellata correttamente!");
    }

    //service creato basandomi su un concetto reale di piattaforma di prenotazione prof
}
