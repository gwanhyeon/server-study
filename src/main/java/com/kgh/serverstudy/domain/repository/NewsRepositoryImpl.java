package com.kgh.serverstudy.domain.repository;

import com.kgh.serverstudy.config.NaverProperties;
import com.kgh.serverstudy.domain.dto.News;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Component
public class NewsRepositoryImpl implements NewsRepository {

    private final RestTemplate restTemplate;
    private final NaverProperties naverProperties;

    public NewsRepositoryImpl(RestTemplate restTemplate, NaverProperties naverProperties) {
        this.restTemplate = restTemplate;
        this.naverProperties = naverProperties;
    }

    @Override
    public News.NewsDto findByQuery(String query) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Naver-Client-Id", naverProperties.getClientId());
        httpHeaders.add("X-Naver-Client-Secret", naverProperties.getClientSecret());

        String url = naverProperties.getNewsUrl() + "?query="+query;
        ResponseEntity<News.NewsDto> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), News.NewsDto.class);
        List<News.Item> newItemList = News.Item.of(exchange);
        News.NewsDto newsDto = News.NewsDto.of(exchange,newItemList);

        return newsDto;
    }
}
