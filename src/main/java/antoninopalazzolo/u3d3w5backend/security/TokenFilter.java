package antoninopalazzolo.u3d3w5backend.security;

import antoninopalazzolo.u3d3w5backend.entities.User;
import antoninopalazzolo.u3d3w5backend.exceptions.UnauthorizedException;
import antoninopalazzolo.u3d3w5backend.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTools tokenTools;
    private final UsersService usersService;

    public TokenFilter(TokenTools tokenTools, UsersService usersService) {
        this.tokenTools = tokenTools;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // controllo che ci sia l'header Authorization nel formato "Bearer token"
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire il token nell'authorization header nel formato corretto");

        // estraggo il token dall'header
        String accessToken = authHeader.replace("Bearer ", "");

        // verifico che il token sia valido
        tokenTools.verifyToken(accessToken);

        // estraggo l'id dal token e cerco l'utente nel db
        UUID userId = this.tokenTools.extractIdFromToken(accessToken);
        User authenticatedUser = this.usersService.findById(userId);

        // associo l'utente al security context così i controller sanno chi sta facendo la richiesta
        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // non controllo il token per le richieste di login e registrazione
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
