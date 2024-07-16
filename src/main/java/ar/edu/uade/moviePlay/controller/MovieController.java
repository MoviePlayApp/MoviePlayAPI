package ar.edu.uade.moviePlay.controller;

import ar.edu.uade.moviePlay.dto.movie.MovieDataDTO;
import ar.edu.uade.moviePlay.service.Impl.MovieDetailsServiceImpl;
import ar.edu.uade.moviePlay.service.Impl.MovieServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import ar.edu.uade.moviePlay.service.MovieService;
import ar.edu.uade.moviePlay.service.MovieDetailsService;

@RestController
public class MovieController {

    private final MovieService movieService;
    private final MovieDetailsService movieDetailsService;

    public MovieController(MovieServiceImpl movieService, MovieDetailsServiceImpl movieDetailsService){
        this.movieService = movieService;
        this.movieDetailsService = movieDetailsService;
    }

    @GetMapping("/movies")
    public GetMovieDTO getMovies(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) String orderByDate,
            @RequestParam(required = false) String orderByRate,
            @RequestParam(required = false) String genre) {

        return movieService.getMovies(page, limit, search, orderByDate, orderByRate, genre);
    }

    @GetMapping("/movies/{movieId}")
    public MovieDataDTO getMovieDetails(
            @PathVariable String movieId) {
        return movieDetailsService.getMovieDetails(movieId);
    }
}
