package ar.edu.uade.moviePlay.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ar.edu.uade.moviePlay.dto.movie.GetMovieDTO;
import reactor.core.publisher.Mono;

@Service
public class MovieServiceImpl implements MovieService {

    private final WebClient webClient;

    @Value("${themoviedb.api.url}")
    private String apiUrl;

    @Value("${themoviedb.api.token}")
    private String apiToken;

    public MovieServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    @Override
    public GetMovieDTO getMovies(int page, int limit, String search, String orderByDate, String orderByRate, String genre) {
        Mono<GetMovieDTO> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/3/movie/popular")
                        .queryParam("page", page)
                        .queryParam("limit", limit)
                        .queryParam("search", search)
                        .queryParam("orderByDate", orderByDate)
                        .queryParam("orderByRate", orderByRate)
                        .queryParam("genre", genre)
                        .build())
                .header("Authorization", "Bearer " + apiToken)
                .retrieve()
                .bodyToMono(GetMovieDTO.class);

        return response.block();
    }
}
