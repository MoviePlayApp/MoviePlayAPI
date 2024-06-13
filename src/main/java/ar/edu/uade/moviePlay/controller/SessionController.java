package ar.edu.uade.moviePlay.controller;

import ar.edu.uade.moviePlay.dto.login.LoginRequestDTO;
import ar.edu.uade.moviePlay.dto.login.LoginResponseDTO;
import ar.edu.uade.moviePlay.dto.logout.LogoutRequestDTO;
import ar.edu.uade.moviePlay.dto.logout.LogoutResponseDTO;
import ar.edu.uade.moviePlay.dto.token.RefreshTokenRequestDTO;
import ar.edu.uade.moviePlay.dto.token.RefreshTokenResponseDTO;
import ar.edu.uade.moviePlay.service.AuthServiceImpl;
import ar.edu.uade.moviePlay.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout(HttpServletRequest request,@RequestBody LogoutRequestDTO logoutRequest) throws GeneralSecurityException, IOException {
        return new ResponseEntity<>(authService.logout(request.getHeader("Authorization"), logoutRequest), HttpStatus.OK);
    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return new ResponseEntity<>(authService.refreshToken(refreshTokenRequestDTO), HttpStatus.OK);
    }
}
