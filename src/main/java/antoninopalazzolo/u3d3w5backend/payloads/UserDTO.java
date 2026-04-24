package antoninopalazzolo.u3d3w5backend.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome deve essere tra 2 e 30 caratteri")
        String name,

        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome deve essere tra 2 e 30 caratteri")
        String surname,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email non è nel formato corretto")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri")
        String password
) {
}
