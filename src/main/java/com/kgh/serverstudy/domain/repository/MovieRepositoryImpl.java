package com.kgh.serverstudy.domain.repository;

import com.kgh.serverstudy.config.NaverProperties;
import com.kgh.serverstudy.domain.dto.MovieGroup;
import com.kgh.serverstudy.domain.dto.ResponseMovie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieRepositoryImpl implements MovieRepository {

    private final RestTemplate restTemplate;
    private final NaverProperties naverProperties;

    public MovieRepositoryImpl(RestTemplate restTemplate, NaverProperties naverProperties) {
        this.restTemplate = restTemplate;
        this.naverProperties = naverProperties;
    }
    public ResponseMovie.MovieDto findByQuery(final String query) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Naver-Client-Id", naverProperties.getClientId());
        httpHeaders.add("X-Naver-Client-Secret", naverProperties.getClientSecret());

        String url = naverProperties.getMovieUrl() + "?query=" + query;
        ResponseEntity<ResponseMovie.MovieDto> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), ResponseMovie.MovieDto.class);

        List<ResponseMovie.Item> ResponseMovieList = ResponseMovie.Item.of(exchange);
        MovieGroup items = new MovieGroup(ResponseMovieList);
        List<ResponseMovie.Item> listOrderRating = items.getListOrderRating();
        ResponseMovie.MovieDto MovieDtoList = ResponseMovie.MovieDto.of(exchange, listOrderRating);
        return MovieDtoList;
    }
}
