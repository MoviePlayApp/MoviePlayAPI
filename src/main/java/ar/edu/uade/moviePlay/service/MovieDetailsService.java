package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.dto.movie.MovieDataDTO;

public interface MovieDetailsService {
    MovieDataDTO getMovieDetails(String movieId, String token);
}
