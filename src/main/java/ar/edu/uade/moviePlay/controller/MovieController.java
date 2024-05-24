package ar.edu.uade.moviePlay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ar.edu.uade.moviePlay.dto.errorResponses.ErrorDTO;
import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import ar.edu.uade.moviePlay.service.MovieService;

public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<GetMovieDTO> getMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) String orderByDate,
            @RequestParam(required = false) String orderByRate,
            @RequestParam(required = false) String genre) {

        try {
            GetMovieDTO movies = movieService.getMovies(page, limit, search, orderByDate, orderByRate, genre);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            ErrorDTO errorResponse = new ErrorDTO();
            errorResponse.setError("Internal Server Error");
            errorResponse.setCode(500);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
}
