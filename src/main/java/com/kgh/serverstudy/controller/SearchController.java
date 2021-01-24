package com.kgh.serverstudy.controller;

import com.kgh.serverstudy.domain.dto.News;
import com.kgh.serverstudy.domain.dto.ResponseMovie;
import com.kgh.serverstudy.service.MovieService;
import com.kgh.serverstudy.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final MovieService movieService;
    private final NewsService newsService;

    @GetMapping("/movies")
    public ResponseMovie.MovieDto getMoviesByQuery(@RequestParam(name = "q") String query){
        return movieService.search(query);
    }
    @GetMapping("/movies/ranking")
    public List<ResponseMovie.Item> getMoviesOrderByQuery(@RequestParam(name = "q") String query){
        return movieService.searchOrderRanking(query);
    }

    @GetMapping("/news")
    public News.NewsDto getNewsByQuery(@RequestParam(name = "q") String query){
        return newsService.findByQuery(query);
    }
}
