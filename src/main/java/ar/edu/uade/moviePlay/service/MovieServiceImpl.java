package ar.edu.uade.moviePlay.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    @Override
    public GetMovieDTO getMovies(int page, int limit, String search, String orderByDate, String orderByRate, String genre) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        Mono<GetMovieDTO> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/discover/movie")
                        .queryParam("language", "en-US")
                        .queryParam("release_date.lte", currentDate)
                        .queryParam("page", page)
                        .queryParam("sort_by", orderByDate)
                        .queryParam("sort_by", orderByRate)
                        .queryParam("with_genres", genre)
                        .build())
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .bodyToMono(GetMovieDTO.class);

        GetMovieDTO getMovieDTO = response.block();

        if (getMovieDTO != null && getMovieDTO.getResults() != null) {
            getMovieDTO.getResults().forEach(movie -> {
                if (movie.getBackdrop_path() != null) {
                    movie.setBackdrop_path(imageBaseUrl + "original" + movie.getBackdrop_path());
                }
            });
        }

        return getMovieDTO;
    }
}
