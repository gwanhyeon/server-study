package com.kgh.serverstudy.service;

import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.domain.dto.MovieGroup;
import com.kgh.serverstudy.domain.dto.ResponseMovie;
import com.kgh.serverstudy.domain.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }
    // DTO안에서 평점 정렬 처리하기, 0 제외
    public ResponseMovie.MovieDto search(final String query){
        return movieRepository.findByQuery(query);
    }

    // 반환시 직접 평점 정렬 처리하기
    public List<ResponseMovie.Item> searchOrderRanking(final String query){
        return movieRepository.findByOrderQuery(query).getItems().stream()
                .filter(b->!((Float)b.getUserRating()).equals(0.0f))
                .sorted((a,b) -> b.getUserRating() > a.getUserRating() ? 1 : -1)
                .collect(Collectors.toList());
    }

    //로컬 임시 테스트
    public List<Movie> query(final String query){
        return Arrays.asList(
                Movie.builder().title("Movie-1").link("http://link").build(),
                Movie.builder().title("Movie-2").link("http://link").build()
                );
    }
    public double calAvgUserRating(String query){
        return getMovieGroup(query).calAvgUserRating().getAsDouble();
    }

    private MovieGroup getMovieGroup(String query){
        return new MovieGroup(findByQueryImpl(query).getItems());
    }

    private ResponseMovie.MovieDto findByQueryImpl(String query) {
        return movieRepository.findByQuery(query);
    }

}
