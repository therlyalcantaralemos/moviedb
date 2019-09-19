package com.moviedb.services;

import com.moviedb.models.themoviedb.CreditTheMovieDB;
import com.moviedb.models.themoviedb.MovieTheMovieDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class TheMovieDBService {

    @Value("${moviedb.api.url}")
    private String apiUrl;
    @Value("${moviedb.api.key}")
    private String apiKey;
    @Value("${moviedb.api.language}")
    private String apiLanguage;
    @Value("${moviedb.api.region}")
    private String apiRegion;
    private final WebClient webClient;
    private final Logger logger = LoggerFactory.getLogger(TheMovieDBService.class);

    @Autowired
    public TheMovieDBService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.build();
    }

    private String getUrlApi(String path, Integer pageNumber){
        return apiUrl + path +
                "?api_key=" + apiKey +
                "&language=" + apiLanguage +
                "&region=" + apiRegion +
                "&page=" + pageNumber +
                "&append_to_response=credits";
    }

    public <T> Flux<T> getApi(String path, Integer pageNumber, Class<T> classRequest){
         return webClient.method(HttpMethod.GET)
                .uri(getUrlApi(path, pageNumber))
                .retrieve()
                .bodyToFlux(classRequest)
                .doOnError(throwable -> logger.error(throwable.getMessage()));
    }

    public MovieTheMovieDB getMovieNowPlayingApi(Integer pageNumber){
        return getApi("movie/now_playing", pageNumber, MovieTheMovieDB.class).blockFirst();
    }

    public CreditTheMovieDB getCreditsAndGenrer(Long id){
        return getApi("/movie/" + id, 1, CreditTheMovieDB.class).blockFirst();
    }
}
