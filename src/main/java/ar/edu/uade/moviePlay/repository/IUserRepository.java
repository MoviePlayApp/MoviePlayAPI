package ar.edu.uade.moviePlay.repository;

import ar.edu.uade.moviePlay.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM users WHERE users.id = :id", nativeQuery = true)
    void deleteById(@Param("id") Integer id);
}
