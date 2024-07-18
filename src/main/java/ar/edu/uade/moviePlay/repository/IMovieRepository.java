package ar.edu.uade.moviePlay.repository;

import ar.edu.uade.moviePlay.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IMovieRepository extends CrudRepository<Movie, Integer> {
    Optional<Movie> findByTmdbId(int id);
}