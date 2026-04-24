package antoninopalazzolo.u3d3w5backend.payloads;


// quando l'utente vuole fare login, manda email e password
// non metto validazioni perché se sbaglia le credenziali ci pensa AuthService a dirgli che ha sbagliato
public record LoginDTO(String email, String password) {
}
