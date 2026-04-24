package antoninopalazzolo.u3d3w5backend.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

// payload che arriva quando un organizzatore vuole creare o modificare un evento
// l'organizzatore non lo mando qui — lo prendo dall'utente autenticato nel service
public record EventDTO(
        @NotBlank(message = "Il titolo è obbligatorio")
        String title,

        @NotBlank(message = "La descrizione è obbligatoria")
        String description,

        @NotNull(message = "La data è obbligatoria")
        @FutureOrPresent(message = "La data deve essere nel futuro")
        LocalDate date,

        @NotBlank(message = "Il luogo è obbligatorio")
        String location,

        @NotNull(message = "Il numero di posti è obbligatorio")
        @Positive(message = "Il numero di posti deve essere positivo")
        int availableSeats
) {
}

