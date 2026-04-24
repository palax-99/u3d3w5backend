package antoninopalazzolo.u3d3w5backend.payloads;

import java.util.UUID;

// risposta quando una prenotazione va a buon fine
// mando l'id della prenotazione, l'evento prenotato e l'utente che ha prenotato
public record BookingRespDTO(UUID id, String eventTitle, String userName) {
}
