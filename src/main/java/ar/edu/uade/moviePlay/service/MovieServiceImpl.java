package ar.edu.uade.moviePlay.service;

import ar.edu.uade.moviePlay.dto.movie.MovieDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final WebClient webClient;

    @Value("${themoviedb.api.token}")
    private String apiToken;

    @Value("${themoviedb.api.url}")
    private String apiUrl;

    @Value("${themoviedb.api.imageBaseUrl}")
    private String imageBaseUrl;

    public MovieServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public GetMovieDTO getMovies(int page, int limit, String search, String orderByDate, String orderByRate, String genre) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        Mono<GetMovieDTO> response;

        if (search.isEmpty()) {
            response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/discover/movie")
                            .queryParam("include_adult", "false")
                            .queryParam("release_date.lte", currentDate)
                            .queryParam("page", page)
                            .queryParam("sort_by", orderByRate != null ? orderByRate : orderByDate != null ? orderByDate : "popularity.desc")
                            .queryParam("with_genres", genre)
                            .build())
                    .header("Authorization", "Bearer " + apiToken)
                    .retrieve()
                    .bodyToMono(GetMovieDTO.class);
        } else {
            response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/movie")
                            .queryParam("query", search)
                            .queryParam("include_adult", "false")
                            .queryParam("page", page)
                            .build())
                    .header("Authorization", "Bearer " + apiToken)
                    .retrieve()
                    .bodyToMono(GetMovieDTO.class);
        }

        GetMovieDTO getMovieDTO = response.block();

        if (getMovieDTO != null && getMovieDTO.getResults() != null) {
            // Ordenar los resultados manualmente
            if (orderByRate != null) {
                if (orderByRate.equals("vote_average.desc")) {
                    getMovieDTO.getResults().sort(Comparator.comparingDouble(MovieDTO::getVote_average));
                } else if (orderByRate.equals("vote_average.asc")) {
                    getMovieDTO.getResults().sort(Comparator.comparingDouble(MovieDTO::getVote_average).reversed());
                }
            } else if (orderByDate != null) {
                if (orderByDate.equals("release_date.desc")) {
                    getMovieDTO.getResults().sort(Comparator.comparing(MovieDTO::getRelease_date));
                } else if (orderByDate.equals("release_date.asc")) {
                    getMovieDTO.getResults().sort(Comparator.comparing(MovieDTO::getRelease_date).reversed());
                }
            }

            getMovieDTO.setResults(
                    getMovieDTO.getResults().stream()
                            .limit(limit)
                            .collect(Collectors.toList())
            );
            getMovieDTO.getResults().forEach(movie -> {
                if (movie.getBackdrop_path() != null) {
                    movie.setBackdrop_path(imageBaseUrl + "original" + movie.getBackdrop_path());
                }
            });
        }

        return getMovieDTO;
    }
}
