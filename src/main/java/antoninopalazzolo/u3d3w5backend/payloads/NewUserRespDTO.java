package antoninopalazzolo.u3d3w5backend.payloads;


import java.util.UUID;

// quando un utente si registra con successo, rispondo con id e email
public record NewUserRespDTO(UUID id, String email) {
}
