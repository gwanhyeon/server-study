package com.kgh.serverstudy.domain.repository.Feign;

import com.kgh.serverstudy.config.NaverConfig.NaverProperties;
import com.kgh.serverstudy.config.RecodeConfig.PerformanceTimeRecord;
import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.domain.dto.MovieGroup;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Configuration
@EnableConfigurationProperties({NaverProperties.class})
public class FeignMovieRepositoryImpl implements FeignMovieRepository {

    private final ConcurrentHashMap<String, Movie.MovieDto> movieMapCache = new ConcurrentHashMap<>();
    private volatile Long cacheLoadTime = 0L;
    private volatile Long cacheTimeLimit = 600 * 1000L;

    /**
     * 영화 쿼리 조회
     * @param exchange
     * @param query
     * @return
     */
    @Override
    @PerformanceTimeRecord
    public Movie.MovieDto findByQuery(Movie.MovieDto exchange, String query) {

        List<Movie.Item> ResponseMovieList = Movie.Item.feignOf(exchange);
        MovieGroup items = new MovieGroup(ResponseMovieList);
        List<Movie.Item> listOrderRating = items.getListOrderRating();
        return Movie.MovieDto.feignOf(exchange, listOrderRating);
    }

    /**
     * 영화 캐시 조회
     * @param query
     * @return
     */
    @Override
    @PerformanceTimeRecord
    public Map<String, Movie.MovieDto> findCacheByQuery(Movie.MovieDto exchange, String query) {
        long nowTime = System.currentTimeMillis();
        if(CollectionUtils.isEmpty(movieMapCache) || nowTime - cacheLoadTime > cacheTimeLimit){
            synchronized (movieMapCache){
                List<Movie.Item> ResponseMovieList = Movie.Item.feignOf(exchange);
                MovieGroup items = new MovieGroup(ResponseMovieList);
                List<Movie.Item> listOrderRating = items.getListOrderRating();
                Movie.MovieDto resultList = Movie.MovieDto.feignOf(exchange, listOrderRating);
                cacheInitialized();
                cacheLoadTime = nowTime;
                movieMapCache.put(query, resultList);
            }
        }
        return movieMapCache;
    }

    /**
     * 영화 캐시 조회
     * @param query
     * @return
     */
    @Override
    @PerformanceTimeRecord
    public Map<String, Movie.MovieDto> findCacheRedisByQuery(Movie.MovieDto exchange, String query) {
        List<Movie.Item> ResponseMovieList = Movie.Item.feignOf(exchange);
        MovieGroup items = new MovieGroup(ResponseMovieList);
        List<Movie.Item> listOrderRating = items.getListOrderRating();
        Movie.MovieDto resultList = Movie.MovieDto.feignOf(exchange, listOrderRating);
        movieMapCache.put(query, resultList);
        return movieMapCache;
    }

    /**
     * 영화 캐시 강제 업데이트
     * @param query
     * @return
     */
    @Override
    @PerformanceTimeRecord
    public Map<String, Movie.MovieDto> saveCacheByQuery(Movie.MovieDto exchange, String query) {
        long nowTime = System.currentTimeMillis();
        if(!CollectionUtils.isEmpty(movieMapCache) || nowTime - cacheLoadTime < cacheTimeLimit){
            synchronized (movieMapCache){
                cacheInitialized();
                List<Movie.Item> ResponseMovieList = Movie.Item.feignOf(exchange);
                MovieGroup items = new MovieGroup(ResponseMovieList);
                List<Movie.Item> listOrderRating = items.getListOrderRating();
                Movie.MovieDto resultList = Movie.MovieDto.feignOf(exchange, listOrderRating);
                cacheLoadTime = nowTime;
                movieMapCache.put(query, resultList);
            }
        }
        return movieMapCache;
    }

    /**
     * 영화 캐시 초기화
     */
    @Override
    public void cacheInitialized() {
        cacheLoadTime = 0L;
        movieMapCache.clear();
    }
    /**
     * 영화 정렬 쿼리 조회
     * @param query
     * @return
     */
    @PerformanceTimeRecord
    public Movie.MovieDto findByOrderQuery(Movie.MovieDto exchange, String query) {
        List<Movie.Item> ResponseMovieList = Movie.Item.feignOf(exchange);
        Movie.MovieDto MovieDtoList = Movie.MovieDto.feignOf(exchange, ResponseMovieList);
        return MovieDtoList;
    }
}