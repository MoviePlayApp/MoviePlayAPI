package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.dto.login.GoogleValidationResponseDTO;
import ar.edu.uade.moviePlay.dto.login.LoginRequestDTO;
import ar.edu.uade.moviePlay.dto.login.LoginResponseDTO;
import ar.edu.uade.moviePlay.entity.RefreshToken;
import ar.edu.uade.moviePlay.entity.User;
import ar.edu.uade.moviePlay.exception.InvalidTokenException;
import ar.edu.uade.moviePlay.repository.IUserRepository;
import io.jsonwebtoken.security.Keys;
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

@Service
public class AuthServiceImpl implements IAuthService{
    IUserRepository userRepository;
    GoogleAuthService googleAuthService;
    IRefreshTokenService refreshTokenService;

    public AuthServiceImpl(IUserRepository userRepository , GoogleAuthService googleAuthService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.googleAuthService = googleAuthService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws GeneralSecurityException, IOException {
        GoogleValidationResponseDTO googleValidationResponse = googleAuthService.verifyToken(loginRequestDTO.getGoogleIdToken());
        if (googleValidationResponse == null) {
            throw new InvalidTokenException("Google Token could not be validated");
        }
        User user = saveNewUser(googleValidationResponse);

        String token = this.getJWTToken(googleValidationResponse.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new LoginResponseDTO(user.getId(), user.getName(), user.getNickName(), user.getEmail(), user.getProfilePictureUri(), token, refreshToken.getToken());
    }

    private User saveNewUser(GoogleValidationResponseDTO googleValidationResponse){
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
        String secretKey = "9a4f2c8d3b7a1e6f45c8a0b3f267d8b1d4e6f3c8a9d2b5f8e3a9c8b5f6v8a3d9";
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
                .expiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
