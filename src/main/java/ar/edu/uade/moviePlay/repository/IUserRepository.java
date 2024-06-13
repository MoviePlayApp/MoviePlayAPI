package ar.edu.uade.moviePlay.repository;

import ar.edu.uade.moviePlay.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users WHERE users.id = :id", nativeQuery = true)
    void deleteById(@Param("id") @Nullable Integer id);
}
