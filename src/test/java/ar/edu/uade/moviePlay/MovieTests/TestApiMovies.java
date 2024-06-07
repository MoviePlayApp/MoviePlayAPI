package ar.edu.uade.moviePlay.MovieTests;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import ar.edu.uade.moviePlay.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
public class TestApiMovies {

    private static final Logger logger = LoggerFactory.getLogger(TestApiMovies.class);

    @Autowired
    private MovieService movieService;

    @Test
    public void testGetMovies() {
        GetMovieDTO movies = movieService.getMovies(1, 10, "", "popularity.desc", null, null);
        logger.info("Movies: {}", movies);
    }
}
