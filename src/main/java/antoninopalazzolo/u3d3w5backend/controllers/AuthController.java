package antoninopalazzolo.u3d3w5backend.controllers;

import antoninopalazzolo.u3d3w5backend.entities.User;
import antoninopalazzolo.u3d3w5backend.exceptions.ValidationException;
import antoninopalazzolo.u3d3w5backend.payloads.LoginDTO;
import antoninopalazzolo.u3d3w5backend.payloads.LoginRespDTO;
import antoninopalazzolo.u3d3w5backend.payloads.NewUserRespDTO;
import antoninopalazzolo.u3d3w5backend.payloads.UserDTO;
import antoninopalazzolo.u3d3w5backend.services.AuthService;
import antoninopalazzolo.u3d3w5backend.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UsersService usersService;

    public AuthController(AuthService authService, UsersService usersService) {
        this.authService = authService;
        this.usersService = usersService;
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        return new LoginRespDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO register(@RequestBody @Validated UserDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }
        User newUser = this.usersService.save(body);
        return new NewUserRespDTO(newUser.getId(), newUser.getEmail());
    }
}
