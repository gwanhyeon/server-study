package com.kgh.serverstudy.service;

import com.kgh.serverstudy.Exception.ExceptionMessage;
import com.kgh.serverstudy.Exception.InvalidRequestException;
import com.kgh.serverstudy.domain.dto.MovieGroup;
import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.domain.repository.MovieRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }
    // 영화 데이터 조회(캐싱X)
    public Movie.MovieDto findByQuery(final String query){
        if(StringUtils.isEmpty(query)){
            throw new InvalidRequestException(ExceptionMessage.INVALID_REQUEST_QUERY_EMPTY_EXCEPTION);
        }
        return movieRepository.findByQuery(query);
    }
    /**
     * 영화 데이터 조회(캐싱)
     * @param query
     * @return
     */
    @Cacheable(value = "cache::movie::query")
    public Map<String, Movie.MovieDto> findCacheByQuery(final String query){
        if(StringUtils.isEmpty(query)){
            throw new InvalidRequestException(ExceptionMessage.INVALID_REQUEST_QUERY_EMPTY_EXCEPTION);
        }
        return movieRepository.findCacheByQuery(query);
    }

    /**
     * 영화 데이터 강제 업데이트
     * @param query
     * @return
     */
    public Map<String, Movie.MovieDto> saveCacheByQuery(final String query){
        if(StringUtils.isEmpty(query)){
            throw new InvalidRequestException(ExceptionMessage.INVALID_REQUEST_QUERY_EMPTY_EXCEPTION);
        }
        return movieRepository.saveCacheByQuery(query);
    }

    /**
     * 영화 평점 반환시 정렬 조회
     * @param query
     * @return
     */
    public List<Movie.Item> searchOrderRanking(final String query){
        if(StringUtils.isEmpty(query)){
            throw new InvalidRequestException(ExceptionMessage.INVALID_REQUEST_QUERY_EMPTY_EXCEPTION);
        }
        return movieRepository.findByOrderQuery(query).getItems().stream()
                .filter(b->!((Float)b.getUserRating()).equals(0.0f))
                .sorted((a,b) -> b.getUserRating() > a.getUserRating() ? 1 : -1)
                .collect(Collectors.toList());
    }

    /**
     * 영화 평점 평균값 반환 조회
     * @param query
     * @return
     */
    public double calAvgUserRating(String query){
        return getMovieGroup(query).calAvgUserRating().getAsDouble();
    }

    /**
     * 영화 그룹 아이템 반환
     * @param query
     * @return
     */
    private MovieGroup getMovieGroup(String query){
        return new MovieGroup(findByQueryImpl(query).getItems());
    }

    /**
     * 영화 조회
     * @param query
     * @return
     */
    private Movie.MovieDto findByQueryImpl(String query) {
        return movieRepository.findByQuery(query);
    }

    /**
     * 로컬 임시 테스트 로직
     * @param query
     * @return
     */
    /*
    public List<Movie> query(final String query){
        return Arrays.asList(
                Movie.builder().title("Movie-1").link("http://link").build(),
                Movie.builder().title("Movie-2").link("http://link").build()
                );
    }
     */

}
