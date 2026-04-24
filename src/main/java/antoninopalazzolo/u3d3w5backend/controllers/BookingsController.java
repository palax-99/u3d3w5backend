package antoninopalazzolo.u3d3w5backend.controllers;

import antoninopalazzolo.u3d3w5backend.entities.Booking;
import antoninopalazzolo.u3d3w5backend.entities.User;
import antoninopalazzolo.u3d3w5backend.payloads.BookingRespDTO;
import antoninopalazzolo.u3d3w5backend.services.BookingsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingsController {

    private final BookingsService bookingsService;

    public BookingsController(BookingsService bookingsService) {
        this.bookingsService = bookingsService;
    }

    // solo gli utenti normali possono prenotare
    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public BookingRespDTO createBooking(@PathVariable UUID eventId,
                                        @AuthenticationPrincipal User currentUser) {
        Booking savedBooking = this.bookingsService.save(eventId, currentUser);
        return new BookingRespDTO(
                savedBooking.getId(),
                savedBooking.getEvent().getTitle(),
                savedBooking.getUser().getName()
        );
    }

    // solo l'utente proprietario può cancellare la propria prenotazione
    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('USER')")
    public void deleteBooking(@PathVariable UUID bookingId,
                              @AuthenticationPrincipal User currentUser) {
        this.bookingsService.findByIdAndDelete(bookingId, currentUser);
    }
}
