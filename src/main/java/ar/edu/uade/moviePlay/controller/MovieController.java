package ar.edu.uade.moviePlay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import ar.edu.uade.moviePlay.service.MovieService;

@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

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
}