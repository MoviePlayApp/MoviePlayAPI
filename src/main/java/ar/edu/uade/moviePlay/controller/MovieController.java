package ar.edu.uade.moviePlay.controller;

import ar.edu.uade.moviePlay.dto.movie.MovieDataDTO;
import ar.edu.uade.moviePlay.dto.movie.MovieRateValueRequestDTO;
import ar.edu.uade.moviePlay.dto.movie.MovieRateValueResponseDTO;
import ar.edu.uade.moviePlay.service.Impl.MovieDetailsServiceImpl;
import ar.edu.uade.moviePlay.service.Impl.MovieServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ar.edu.uade.moviePlay.service.Impl.RatingServiceImpl;
import ar.edu.uade.moviePlay.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import ar.edu.uade.moviePlay.service.MovieService;
import ar.edu.uade.moviePlay.service.MovieDetailsService;

@RestController
public class MovieController {

    private final MovieService movieService;
    private final MovieDetailsService movieDetailsService;
    private final RatingService ratingService;

    public MovieController(MovieServiceImpl movieService, MovieDetailsServiceImpl movieDetailsService, RatingServiceImpl ratingService){
        this.movieService = movieService;
        this.movieDetailsService = movieDetailsService;
        this.ratingService = ratingService;
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
            HttpServletRequest request,
            @PathVariable String movieId) {
        return movieDetailsService.getMovieDetails(movieId, request.getHeader("Authorization"));
    }

    @PutMapping("/movies/{movieId}/rating")
    @ResponseStatus(HttpStatus.OK)
    public MovieRateValueResponseDTO updateMovieRating(
            @PathVariable String movieId, @RequestBody MovieRateValueRequestDTO request, HttpServletRequest requestHttp) {
        String updateStatus = ratingService.updateMovieRating(movieId, request.getRateValue());
        MovieDataDTO updatedMovieDetails = movieDetailsService.getMovieDetails(movieId, requestHttp.getHeader("Authorization"));
        return MovieRateValueResponseDTO.builder()
                .movieId(Integer.parseInt(movieId))
                .movieRate(updatedMovieDetails.getVote_average())
                .statusMessage(updateStatus)
                .build();
    }

}
