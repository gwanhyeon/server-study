package com.kgh.serverstudy.domain.repository.Feign;

import com.kgh.serverstudy.config.AbstractCustomCache;
import com.kgh.serverstudy.config.NaverProperties;
import com.kgh.serverstudy.config.PerformanceTimeRecord;
import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.domain.dto.MovieGroup;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Configuration
@EnableConfigurationProperties({NaverProperties.class})
public class FeignMovieRepositoryImpl extends AbstractCustomCache implements FeignMovieRepository {

    private final ConcurrentHashMap<String, Movie.MovieDto> movieMapCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Movie.MovieDto> store;
    private volatile Long cacheLoadTime = 0L;
    private volatile Long cacheTimeLimit = 600 * 1000L;

    public FeignMovieRepositoryImpl(ConcurrentMap<String, Movie.MovieDto> store) {
        this.store = store;
    }

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

    @Override
    protected Optional<Object> lookup(Object key) {
        return Optional.ofNullable(this.store.get(key));
    }

    @Override
    public boolean put(String key, Movie.MovieDto value) {
        this.store.put(key, value);
        return true;
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }
}