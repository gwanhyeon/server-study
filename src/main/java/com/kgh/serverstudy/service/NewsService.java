package com.kgh.serverstudy.service;

import com.kgh.serverstudy.domain.dto.News;
import com.kgh.serverstudy.domain.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public News.NewsDto findByQuery(String query){
        return newsRepository.findByQuery(query);
    }



}
