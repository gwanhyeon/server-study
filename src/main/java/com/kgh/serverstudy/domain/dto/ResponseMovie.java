package com.kgh.serverstudy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMovie {
    private List<Item> items;
    private MovieDto movieDto;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MovieDto {
        private String lastBuilderDate;
        private Long total;
        private Long start;
        private Long display;
        private List<ResponseMovie.Item> items;
        // todo field Adding
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String title;
        private String link;
        private String image;
        private String subtitle;
        private String pubDate;
        private String director;
        private String actor;
        private float userRating;
        // todo field Adding
    }
}
