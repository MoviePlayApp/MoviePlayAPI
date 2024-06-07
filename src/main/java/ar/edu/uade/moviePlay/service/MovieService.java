package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;

public interface MovieService {
    GetMovieDTO getMovies(int page, int limit, String search, String orderByDate, String orderByRate, String genre);
}
