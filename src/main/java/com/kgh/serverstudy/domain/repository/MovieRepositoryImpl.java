package com.kgh.serverstudy.domain.repository;

import com.kgh.serverstudy.Exception.ExceptionMessage;
import com.kgh.serverstudy.Exception.OpenApiRuntimeException;
import com.kgh.serverstudy.config.NaverConfig.NaverProperties;
import com.kgh.serverstudy.domain.dto.MovieGroup;
import com.kgh.serverstudy.domain.dto.Movie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public
class MovieRepositoryImpl implements MovieRepository {

    private final RestTemplate restTemplate;
    private final NaverProperties naverProperties;
    private final ConcurrentHashMap<String, Movie.MovieDto> movieMapCache = new ConcurrentHashMap<>(16);
    private volatile Long cacheLoadTime = 0L;
    private volatile Long cacheTimeLimit = 600 * 1000L;
    public MovieRepositoryImpl(NaverProperties naverProperties, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.naverProperties = naverProperties;
    }

    /**
     * 영화 쿼리 조회
     * @param query
     * @return
     */
    public Movie.MovieDto findByQuery(final String query) {
        ResponseEntity<Movie.MovieDto> exchange = getExchange(query);
        List<Movie.Item> ResponseMovieList = Movie.Item.of(exchange);
        MovieGroup items = new MovieGroup(ResponseMovieList);
        List<Movie.Item> listOrderRating = items.getListOrderRating();
        return Movie.MovieDto.of(exchange, listOrderRating);
    }

    /**
     * 영화 캐시 조회
     * @param query
     * @return
     */
    public Map<String, Movie.MovieDto> findCacheByQuery(final String query) {
        long nowTime = System.currentTimeMillis();
        if(CollectionUtils.isEmpty(movieMapCache) || nowTime - cacheLoadTime > cacheTimeLimit){
            synchronized (movieMapCache){
                ResponseEntity<Movie.MovieDto> exchange = getExchange(query);
                List<Movie.Item> ResponseMovieList = Movie.Item.of(exchange);
                MovieGroup items = new MovieGroup(ResponseMovieList);
                List<Movie.Item> listOrderRating = items.getListOrderRating();
                Movie.MovieDto resultList = Movie.MovieDto.of(exchange, listOrderRating);
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
    public Map<String, Movie.MovieDto> saveCacheByQuery(final String query) {
        long nowTime = System.currentTimeMillis();
        if(!CollectionUtils.isEmpty(movieMapCache) || nowTime - cacheLoadTime < cacheTimeLimit){
            synchronized (movieMapCache){
                cacheInitialized();
                ResponseEntity<Movie.MovieDto> exchange = getExchange(query);
                List<Movie.Item> ResponseMovieList = Movie.Item.of(exchange);
                MovieGroup items = new MovieGroup(ResponseMovieList);
                List<Movie.Item> listOrderRating = items.getListOrderRating();
                Movie.MovieDto resultList = Movie.MovieDto.of(exchange, listOrderRating);
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
    public Movie.MovieDto findByOrderQuery(final String query) {
        ResponseEntity<Movie.MovieDto> exchange = getExchange(query);
        List<Movie.Item> ResponseMovieList = Movie.Item.of(exchange);
        Movie.MovieDto MovieDtoList = Movie.MovieDto.of(exchange, ResponseMovieList);
        return MovieDtoList;
    }

    /**
     * NAVER OPEN API 통신 연결
     * @param query
     * @return
     */
    private ResponseEntity<Movie.MovieDto> getExchange(String query) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Naver-Client-Id", naverProperties.getClientId());
        httpHeaders.add("X-Naver-Client-Secret", naverProperties.getClientSecret());
        String url = naverProperties.getMovieUrl() + "?query=" + query;
        try{
            return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), Movie.MovieDto.class);
        }catch (HttpClientErrorException e){
            throw new OpenApiRuntimeException(ExceptionMessage.NAVER_API_ERROR_EXCEPTION);
        }catch(Exception ee){
            throw new OpenApiRuntimeException(ExceptionMessage.NAVER_API_UNAUTHORIZED_EXCEPTION);
        }
    }
}