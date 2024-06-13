package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.dto.login.GoogleValidationResponseDTO;
import ar.edu.uade.moviePlay.dto.login.LoginRequestDTO;
import ar.edu.uade.moviePlay.dto.login.LoginResponseDTO;
import ar.edu.uade.moviePlay.dto.logout.LogoutRequestDTO;
import ar.edu.uade.moviePlay.dto.logout.LogoutResponseDTO;
import ar.edu.uade.moviePlay.dto.token.RefreshTokenRequestDTO;
import ar.edu.uade.moviePlay.dto.token.RefreshTokenResponseDTO;
import ar.edu.uade.moviePlay.entity.RefreshToken;
import ar.edu.uade.moviePlay.entity.User;
import ar.edu.uade.moviePlay.exception.InvalidTokenException;
import ar.edu.uade.moviePlay.exception.NotFoundException;
import ar.edu.uade.moviePlay.repository.IUserRepository;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.jsonwebtoken.Jwts;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements IAuthService{
    private final InMemoryTokenBlacklist inMemoryTokenBlacklist;
    IUserRepository userRepository;
    GoogleAuthService googleAuthService;
    IRefreshTokenService refreshTokenService;
    @Value("${secret.jwt.key}")
    private String SECRET_JWT_KEY;

    public AuthServiceImpl(IUserRepository userRepository , GoogleAuthService googleAuthService, RefreshTokenService refreshTokenService, InMemoryTokenBlacklist inMemoryTokenBlacklist) {
        this.userRepository = userRepository;
        this.googleAuthService = googleAuthService;
        this.refreshTokenService = refreshTokenService;
        this.inMemoryTokenBlacklist = inMemoryTokenBlacklist;
    }

    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws GeneralSecurityException, IOException {
        GoogleValidationResponseDTO googleValidationResponse = googleAuthService.verifyToken(loginRequestDTO.getGoogleIdToken());
        if (googleValidationResponse == null) {
            throw new InvalidTokenException("Google Token could not be validated");
        }
        User user = handleGoogleValidationReponse(googleValidationResponse);

        String token = this.getJWTToken(googleValidationResponse.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new LoginResponseDTO(user.getId(), user.getName(), user.getNickName(), user.getEmail(), user.getProfilePictureUri(), token, refreshToken.getToken());
    }

    @Override
    public LogoutResponseDTO logout(String token, LogoutRequestDTO logoutRequestDTO) {
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            throw new InvalidTokenException("Invalid token");
        }
        inMemoryTokenBlacklist.addToBlacklist(token.substring(7));
        return new LogoutResponseDTO(logoutRequestDTO.getEmail(), "Logout successful");
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.getRefreshToken());
        if (refreshToken.isEmpty()) {
            throw new NotFoundException("User or refresh token is not valid.");
        }
        String token = this.getJWTToken(refreshToken.get().getUser().getEmail());
        return new RefreshTokenResponseDTO("Success",token, refreshTokenRequestDTO.getRefreshToken());
    }

    @Transactional
    public User handleGoogleValidationReponse(GoogleValidationResponseDTO googleValidationResponse){
        Optional<User> user = userRepository.findByEmail(googleValidationResponse.getEmail());
        if (user.isPresent()){
            return user.get();
        }
        User newUser = new User();
        newUser.setEmail(googleValidationResponse.getEmail());
        newUser.setName(googleValidationResponse.getName());
        newUser.setNickName(null);
        newUser.setProfilePictureUri(googleValidationResponse.getImageUri());

        userRepository.save(newUser);
        return newUser;
    }

    private String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        return Jwts
                .builder()
                .id("MoviePlayAPI")
                .subject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(SECRET_JWT_KEY.getBytes()))
                .compact();
    }
}
