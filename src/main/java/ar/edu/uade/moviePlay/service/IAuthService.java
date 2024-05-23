package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.dto.login.LoginRequestDTO;
import ar.edu.uade.moviePlay.dto.login.LoginResponseDTO;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws GeneralSecurityException, IOException;
}
