package ar.edu.uade.moviePlay.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import ar.edu.uade.moviePlay.dto.movie.MovieDTO;

@Service
public class MovieService {

    @Value("${tmdb.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    
    public GetMovieDTO getMovies(int page, int limit, String search, String orderByDate, String orderByRate, String genre) {
        String url = UriComponentsBuilder.fromHttpUrl("https://api.themoviedb.org/3/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", search)
                .queryParam("page", page + 1) // TMDb paginación empieza en 1
                .toUriString();

        TMDbResponse response = restTemplate.getForObject(url, TMDbResponse.class);

        List<MovieDTO> movies = Arrays.stream(response.getResults())
                .map(result -> {
                    MovieDTO dto = new MovieDTO();
                    dto.setId(result.getId());
                    dto.setImageUri(result.getPosterPath());
                    return dto;
                })
                .collect(Collectors.toList());

        GetMovieDTO getMoviesDTO = new GetMovieDTO();
        getMoviesDTO.setMovies(movies);

        return getMoviesDTO;
    }

    // Clase interna para mapear la respuesta de TMDb
    private static class TMDbResponse {
        private TMDbMovie[] results;
        // Getters and setters

        private static class TMDbMovie {
            private String id;
            private String posterPath;
            // Getters and setters
        }
    }
    
}
