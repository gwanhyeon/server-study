package com.kgh.serverstudy.controller;

import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.domain.dto.News;
import com.kgh.serverstudy.service.FeignMovieService;
import com.kgh.serverstudy.service.MovieService;
import com.kgh.serverstudy.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class FeignController {

    private final FeignMovieService movieService;
//    private final NewsService newsService;

    @GetMapping("/feign-movies")
    public Movie.MovieDto getFeignMoviesByQuery(@RequestParam(name = "q") String query){
        return movieService.findByQuery(query);
    }
    @GetMapping("/feign-movies/caches")
    public Map<String, Movie.MovieDto> getFeignMoviesCacheByQuery(@RequestParam(name = "q") String query){
        return movieService.findCacheByQuery(query);
    }

    @PostMapping("/feign-movies")
    public Map<String, Movie.MovieDto> updateFeignMoviesCacheByQuery(@RequestParam(name = "q") String query){
        return movieService.saveCacheByQuery(query);
    }

    @GetMapping("/feign-movies/ranking")
    public List<Movie.Item> getFeignMoviesOrderByQuery(@RequestParam(name = "q") String query){
        return movieService.searchOrderRanking(query);
    }

//    @GetMapping("/feign-news")
//    public News.NewsDto getNewsByQuery(@RequestParam(name = "q") String query){
//        return newsService.findByQuery(query);
//    }
}
