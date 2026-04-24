package antoninopalazzolo.u3d3w5backend.services;

import antoninopalazzolo.u3d3w5backend.entities.User;
import antoninopalazzolo.u3d3w5backend.exceptions.NotFoundException;
import antoninopalazzolo.u3d3w5backend.exceptions.UnauthorizedException;
import antoninopalazzolo.u3d3w5backend.payloads.LoginDTO;
import antoninopalazzolo.u3d3w5backend.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsersService usersService;
    private final TokenTools tokenTools;
    private final PasswordEncoder bcrypt;

    public AuthService(UsersService usersService, TokenTools tokenTools, PasswordEncoder bcrypt) {
        this.usersService = usersService;
        this.tokenTools = tokenTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        // se non trovo l'email dico solo "credenziali erraate" così non do info a chi prova ad entrarer
        try {
            User found = this.usersService.findByEmail(body.email());
            // controllo che la password corrisponda
            if (this.bcrypt.matches(body.password(), found.getPassword())) {
                // credenziali ok — genero e ritorno il token
                return this.tokenTools.generateToken(found);
            } else {
                throw new UnauthorizedException("Credenziali errate");
            }
        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}
