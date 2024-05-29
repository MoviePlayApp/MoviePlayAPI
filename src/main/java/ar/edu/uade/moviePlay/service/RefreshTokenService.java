package ar.edu.uade.moviePlay.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.uade.moviePlay.entity.RefreshToken;
import ar.edu.uade.moviePlay.entity.User;
import ar.edu.uade.moviePlay.repository.IRefreshTokenRepository;
import ar.edu.uade.moviePlay.repository.IUserRepository;

@Service
public class RefreshTokenService implements IRefreshTokenService{

    @Autowired
    IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    IUserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(User user){
        Optional<RefreshToken> token = refreshTokenRepository.findByUser(user);
        if(token.isEmpty()){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByEmail(user.getEmail()).orElseThrow())
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file
                .build();
        return refreshTokenRepository.save(refreshToken);
        }
        else{
            return token.get();
        }
    }

    @Override
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

    public void revokeRefreshToken(Integer tokenId){
        refreshTokenRepository.deleteById(tokenId);
    }
}
