package com.kgh.serverstudy.service;

import com.kgh.serverstudy.Repository.MovieRepository;
import com.kgh.serverstudy.domain.Movie;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }
    public List<Movie> search(final String query){
        return movieRepository.findByQuery(query);
    }

    // 로컬 임시 테스트
    public List<Movie> query(final String query){
        return Arrays.asList(
                Movie.builder().title("Movie-1").link("http://link").build(),
                Movie.builder().title("Movie-2").link("http://link").build()
                );
    }
}
