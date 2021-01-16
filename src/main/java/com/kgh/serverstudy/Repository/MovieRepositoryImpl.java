package com.kgh.serverstudy.Repository;

import com.kgh.serverstudy.config.NaverProperties;
import com.kgh.serverstudy.domain.Movie;
import com.kgh.serverstudy.domain.ResponseMovie;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


import java.util.stream.Collectors;

@Component
public class MovieRepositoryImpl implements MovieRepository {

    private final RestTemplate restTemplate;
    private final NaverProperties naverProperties;

    public MovieRepositoryImpl(RestTemplate restTemplate, NaverProperties naverProperties) {
        this.restTemplate = restTemplate;
        this.naverProperties = naverProperties;
    }

    public List<Movie> findByQuery(final String query) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Naver-Client-Id", naverProperties.getClientId());
        httpHeaders.add("X-Naver-Client-Secret", naverProperties.getClientSecret());

        String url = naverProperties.getMovieUrl() + "?query=" + query;

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), ResponseMovie.class)
                .getBody()
                .getItems()
                .stream()
                .map(m -> Movie.builder()
                        .title(m.getTitle())
                        .link(m.getLink())
                        .userRating(m.getUserRating())
                        .build())
                .collect(Collectors.toList());

    }

}
