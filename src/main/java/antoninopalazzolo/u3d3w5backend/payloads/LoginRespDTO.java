package antoninopalazzolo.u3d3w5backend.payloads;

// quando il login va a buon fine, rispondo con il token JWT
// il client lo salva e lo manda ad ogni richiesta nell'header Authorization
public record LoginRespDTO(String accessToken) {
}
