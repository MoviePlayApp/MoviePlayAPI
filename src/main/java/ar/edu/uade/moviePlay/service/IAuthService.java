package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.dto.login.LoginRequestDTO;
import ar.edu.uade.moviePlay.dto.login.LoginResponseDTO;
import ar.edu.uade.moviePlay.dto.logout.LogoutRequestDTO;
import ar.edu.uade.moviePlay.dto.logout.LogoutResponseDTO;
import ar.edu.uade.moviePlay.dto.token.RefreshTokenRequestDTO;
import ar.edu.uade.moviePlay.dto.token.RefreshTokenResponseDTO;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IAuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws GeneralSecurityException, IOException;
    LogoutResponseDTO logout(String token, LogoutRequestDTO loginRequestDTO) throws GeneralSecurityException, IOException;
    RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
}
