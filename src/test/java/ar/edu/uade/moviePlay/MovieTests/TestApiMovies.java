package ar.edu.uade.moviePlay.MovieTests;

import ar.edu.uade.moviePlay.service.MovieDetailsService;
import ar.edu.uade.moviePlay.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("local")
public class TestApiMovies {

    private static final Logger logger = LoggerFactory.getLogger(TestApiMovies.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieDetailsService movieDetailsService;
}
