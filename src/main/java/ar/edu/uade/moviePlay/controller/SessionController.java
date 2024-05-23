package ar.edu.uade.moviePlay.controller;

import ar.edu.uade.moviePlay.dto.login.LoginRequestDTO;
import ar.edu.uade.moviePlay.dto.login.LoginResponseDTO;
import ar.edu.uade.moviePlay.service.AuthServiceImpl;
import ar.edu.uade.moviePlay.service.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final IAuthService authService;

    public SessionController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) throws GeneralSecurityException, IOException {
        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }
}
