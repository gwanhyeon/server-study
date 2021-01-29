package com.kgh.serverstudy.domain.repository.Feign;
import com.kgh.serverstudy.domain.dto.Movie;

import java.util.Map;

public interface FeignMovieRepository {
    Movie.MovieDto findByQuery(Movie.MovieDto exchange, String query);
    Movie.MovieDto findByOrderQuery(Movie.MovieDto exchange, String query);
    Map<String, Movie.MovieDto> saveCacheByQuery(Movie.MovieDto exchange, String query);
    Map<String, Movie.MovieDto> findCacheByQuery(Movie.MovieDto exchange, String query);
    void cacheInitialized();
}
