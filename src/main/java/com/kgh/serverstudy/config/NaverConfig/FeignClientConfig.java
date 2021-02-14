package com.kgh.serverstudy.config.NaverConfig;

import com.kgh.serverstudy.Exception.ExceptionMessage;
import com.kgh.serverstudy.domain.dto.Movie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@FeignClient(name = "naver-openapi-url", url = "${naver.openapi.url}")
public interface FeignClientConfig {
    @GetMapping(value = "/v1/search/movie.json",
            produces = "application/json")
    Movie.MovieDto getFeignMoviesByQuery(@RequestHeader("X-Naver-Client-Id") String clientId,
                                         @RequestHeader("X-Naver-Client-Secret") String clientSecret,
                                         @RequestParam(name = "query") String query);

    @GetMapping(value = "/v1/search/movie.json", produces = "application/json")
    Map<String, Movie.MovieDto> getFeignMoviesCacheByQuery(@RequestHeader("X-Naver-Client-Id") String clientId,
                                                           @RequestHeader("X-Naver-Client-Secret") String clientSecret,
                                                           @RequestParam(name = "query") String query);

    @PostMapping(value = "/v1/search/movie.json", produces = "application/json")
    Map<String, Movie.MovieDto> updateFeignMoviesCacheByQuery(@RequestHeader("X-Naver-Client-Id") String clientId,
                                                              @RequestHeader("X-Naver-Client-Secret") String clientSecret,
                                                              @RequestParam(name = "query") String query);

    @GetMapping(value = "/v1/search/movie.json", produces = "application/json")
    List<Movie.Item> getFeignMoviesOrderByQuery(@RequestHeader("X-Naver-Client-Id") String clientId,
                                                @RequestHeader("X-Naver-Client-Secret") String clientSecret,
                                                @RequestParam(name = "query") String query);
}