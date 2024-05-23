package ar.edu.uade.moviePlay.repository;

import ar.edu.uade.moviePlay.entity.RefreshToken;
import ar.edu.uade.moviePlay.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByToken(String refreshToken);
}
