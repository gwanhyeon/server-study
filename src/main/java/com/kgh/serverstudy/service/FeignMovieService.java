package com.kgh.serverstudy.service;

import com.kgh.serverstudy.Exception.ExceptionMessage;
import com.kgh.serverstudy.Exception.InvalidRequestException;
import com.kgh.serverstudy.config.NaverConfig.FeignClientConfig;
import com.kgh.serverstudy.config.NaverConfig.NaverProperties;
import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.domain.dto.MovieGroup;
import com.kgh.serverstudy.domain.repository.Feign.FeignMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Configuration
@EnableConfigurationProperties({NaverProperties.class})
@RequiredArgsConstructor
public class FeignMovieService {

    private final NaverProperties naverProperties;
    private final FeignClientConfig feignClientConfig;
    private final FeignMovieRepository feignMovieRepository;

    // 영화 데이터 조회(캐싱X)
    public Movie.MovieDto findByQuery(final String query){
        if(StringUtils.isEmpty(query)){
            throw new InvalidRequestException(ExceptionMessage.INVALID_REQUEST_QUERY_EMPTY_EXCEPTION);
        }
        Movie.MovieDto moviesByQuery = feignClientConfig.getFeignMoviesByQuery(naverProperties.getClientId(), naverProperties.getClientSecret(), query);
        return feignMovieRepository.findByQuery(moviesByQuery,query);
    }
    /**
     * 영화 데이터 조회(캐싱)
     * @param query
     * @return
     */

    public Map<String, Movie.MovieDto> findCacheByQuery(final String query){
        if(StringUtils.isEmpty(query)){
            throw new InvalidRequestException(ExceptionMessage.INVALID_REQUEST_QUERY_EMPTY_EXCEPTION);
        }
        Movie.MovieDto moviesByQuery = feignClientConfig.getFeignMoviesByQuery(naverProperties.getClientId(), naverProperties.getClientSecret(), query);
        return feignMovieRepository.findCacheByQuery(moviesByQuery, query);
    }

    /**
     * 영화 데이터 조회(캐시 추상화 - 레디스)
     * @param query
     * @return
     */
    @Cacheable(value = "cache::movie::query")
    public Map<String, Movie.MovieDto> findCacheRedisByQuery(final String query){
        Movie.MovieDto moviesByQuery = feignClientConfig.getFeignMoviesByQuery(naverProperties.getClientId(), naverProperties.getClientSecret(), query);
        return feignMovieRepository.findCacheRedisByQuery(moviesByQuery, query);
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
        Movie.MovieDto moviesByQuery = feignClientConfig.getFeignMoviesByQuery(naverProperties.getClientId(), naverProperties.getClientSecret(), query);
        return feignMovieRepository.saveCacheByQuery(moviesByQuery, query);
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
        Movie.MovieDto moviesByQuery = feignClientConfig.getFeignMoviesByQuery(naverProperties.getClientId(), naverProperties.getClientSecret(), query);
        return feignMovieRepository.findByOrderQuery(moviesByQuery,query).getItems().stream()
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
        Movie.MovieDto moviesByQuery = feignClientConfig.getFeignMoviesByQuery(naverProperties.getClientId(), naverProperties.getClientSecret(), query);
        return feignMovieRepository.findByQuery(moviesByQuery, query);
    }
}
