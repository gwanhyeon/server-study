package com.kgh.serverstudy.controller;

import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.domain.dto.ResponseMovie;
import com.kgh.serverstudy.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public List<ResponseMovie.Item> getMoviesByQuery(@RequestParam(name = "q") String query){
        return movieService.search(query);
    }
}
