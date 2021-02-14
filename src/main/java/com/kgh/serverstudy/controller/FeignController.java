package com.kgh.serverstudy.controller;
import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.service.FeignMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class FeignController {

    private final FeignMovieService movieService;
    private final CacheManager cacheManager;
//    private final NewsService newsService;


    @GetMapping("/feign-movies")
    public Movie.MovieDto getFeignMoviesByQuery(@RequestParam(name = "q") String query){
        return movieService.findByQuery(query);
    }
    @GetMapping("/feign-movies/caches")
    public Map<String, Movie.MovieDto> getFeignMoviesCacheByQuery(@RequestParam(name = "query") String query){
        return movieService.findCacheRedisByQuery(query);
    }

    @PostMapping("/feign-movies")
    public Map<String, Movie.MovieDto> updateFeignMoviesCacheByQuery(@RequestParam(name = "query") String query){
        return movieService.saveCacheByQuery(query);
    }

    @GetMapping("/feign-movies/ranking")
    public List<Movie.Item> getFeignMoviesOrderByQuery(@RequestParam(name = "q") String query){
        return movieService.searchOrderRanking(query);
    }

    @CacheEvict(value="cache::movie::query", key="#query")
    @DeleteMapping("/feign-movies")
    public boolean clearMovieQueryCache(@RequestParam(name="query")String query){
        return true;
    }



//    @GetMapping("/feign-news")
//    public News.NewsDto getNewsByQuery(@RequestParam(name = "q") String query){
//        return newsService.findByQuery(query);
//    }
}
