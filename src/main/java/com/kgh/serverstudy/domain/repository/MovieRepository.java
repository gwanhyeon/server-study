package com.kgh.serverstudy.domain.repository;
import com.kgh.serverstudy.domain.dto.Movie;

import java.util.Map;

public interface MovieRepository {
    Movie.MovieDto findByQuery(String query);
    Movie.MovieDto findByOrderQuery(String query);
    Map<String, Movie.MovieDto> saveCacheByQuery(final String query);
    Map<String, Movie.MovieDto> findCacheByQuery(String query);
    void cacheInitialized();
}
