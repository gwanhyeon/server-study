package com.kgh.serverstudy.domain.repository;

import com.kgh.serverstudy.domain.dto.News;

import java.util.List;

public interface NewsRepository {
    News.NewsDto findByQuery(String Query);
}
