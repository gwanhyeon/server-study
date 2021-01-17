package com.kgh.serverstudy.domain.dto;

import lombok.*;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class News implements Serializable {
    private List<News.Item> items;
    private News.NewsDto newsDto;

    public News(List<News.Item> items, NewsDto newsDto) {
        this.items = items;
        this.newsDto = newsDto;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewsDto{

        private Long total;
        private Long start;
        private Long display;
        private String lastBuildDate;
        private List<News.Item> items;

        public static News.NewsDto of(ResponseEntity<News.NewsDto> newsDtoResponseEntity, List<News.Item> item){
            return News.NewsDto.builder()
                    .start(newsDtoResponseEntity.getBody().getStart())
                    .display(newsDtoResponseEntity.getBody().getDisplay())
                    .total(newsDtoResponseEntity.getBody().getTotal())
                    .lastBuildDate(newsDtoResponseEntity.getBody().getLastBuildDate())
                    .items(item)
                    .build();
        }

    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String title;
        private String originallink;
        private String link;
        private String pubDate;
        private String description;

         public static List<News.Item> of(ResponseEntity<News.NewsDto> exchange){
            return exchange.getBody()
                    .getItems()
                    .stream()
                    .map(m -> News.Item.builder()
                    .title(m.getTitle())
                    .link(m.getLink())
                    .pubDate(m.getPubDate())
                    .originallink(m.getOriginallink())
                    .description(m.getDescription())
                    .build())
                    .collect(Collectors.toList());
        }
    }
}
