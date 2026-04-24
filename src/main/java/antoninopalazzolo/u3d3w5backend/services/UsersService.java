package antoninopalazzolo.u3d3w5backend.services;

import antoninopalazzolo.u3d3w5backend.entities.User;
import antoninopalazzolo.u3d3w5backend.exceptions.BadRequestException;
import antoninopalazzolo.u3d3w5backend.exceptions.NotFoundException;
import antoninopalazzolo.u3d3w5backend.payloads.UserDTO;
import antoninopalazzolo.u3d3w5backend.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder bcrypt;

    public UsersService(UsersRepository usersRepository, PasswordEncoder bcrypt) {
        this.usersRepository = usersRepository;
        this.bcrypt = bcrypt;
    }

    public User save(UserDTO body) {
        if (this.usersRepository.existsByEmail(body.email()))
            throw new BadRequestException("L'email " + body.email() + " è già in uso!");

        User newUser = new User(body.name(), body.surname(), body.email(), this.bcrypt.encode(body.password()));
        User savedUser = this.usersRepository.save(newUser);

        log.info("Utente con id " + savedUser.getId() + " salvato correttamente!");
        return savedUser;
    }

    public User findById(UUID userId) {
        return this.usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByEmail(String email) {
        return this.usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }
}
