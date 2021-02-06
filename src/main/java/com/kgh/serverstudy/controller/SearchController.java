package com.kgh.serverstudy.controller;

import com.kgh.serverstudy.domain.dto.News;
import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.service.MovieService;
import com.kgh.serverstudy.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final MovieService movieService;
    private final NewsService newsService;

    @GetMapping("/movies")
    public Movie.MovieDto getMoviesByQuery(@RequestParam(name = "q") String query){
        return movieService.findByQuery(query);
    }
    @GetMapping("/movies/caches")
    public Map<String, Movie.MovieDto> getMoviesCacheByQuery(@RequestParam(name = "q") String query){
        return movieService.findCacheByQuery(query);
    }

    @PostMapping("/movies")
    public Map<String, Movie.MovieDto> updateMoviesCacheByQuery(@RequestParam(name = "q") String query){
        return movieService.saveCacheByQuery(query);
    }

    @GetMapping("/movies/ranking")
    public List<Movie.Item> getMoviesOrderByQuery(@RequestParam(name = "q") String query){
        return movieService.searchOrderRanking(query);
    }

    @GetMapping("/news")
    public News.NewsDto getNewsByQuery(@RequestParam(name = "q") String query){
        return newsService.findByQuery(query);
    }
}
