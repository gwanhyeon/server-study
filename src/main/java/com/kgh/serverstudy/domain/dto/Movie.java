package com.kgh.serverstudy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    private List<Item> items;
    private MovieDto movieDto;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MovieDto {
        private String lastBuildDate;
        private Long total;
        private Long start;
        private Long display;
        private List<Movie.Item> items;
        // todo field Adding

        public static Movie.MovieDto of(ResponseEntity<MovieDto> exchange, List<Item> listOrderRating){
            return Movie.MovieDto.builder()
                    .display(exchange.getBody().getDisplay())
                    .start(exchange.getBody().getStart())
                    .lastBuildDate(exchange.getBody().getLastBuildDate())
                    .total(exchange.getBody().getTotal())
                    .items(listOrderRating)
                    .build();
        }
        public static Movie.MovieDto feignOf(MovieDto exchange, List<Item> listOrderRating){
            return Movie.MovieDto.builder()
                    .display(exchange.getDisplay())
                    .start(exchange.getStart())
                    .lastBuildDate(exchange.getLastBuildDate())
                    .total(exchange.getTotal())
                    .items(listOrderRating)
                    .build();
        }

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
        public String getTitle() {
            return deleteTagB(title);
        }

        public static List<Movie.Item> of(ResponseEntity<MovieDto> exchange) {
            return exchange
                    .getBody()
                    .getItems()
                    .stream()
                    .map(m -> Movie.Item.builder()
                            .title(deleteTagB(m.getTitle()))
                            .link(m.getLink())
                            .image(m.getImage())
                            .director(m.getDirector())
                            .pubDate(m.getPubDate())
                            .subtitle(m.getSubtitle())
                            .userRating(m.getUserRating())
                            .actor(m.getActor())
                            .build())
                    .collect(Collectors.toList());
        }
        public static List<Movie.Item> feignOf(Movie.MovieDto exchange) {
            return exchange
                    .getItems()
                    .stream()
                    .map(m -> Movie.Item.builder()
                            .title(deleteTagB(m.getTitle()))
                            .link(m.getLink())
                            .image(m.getImage())
                            .director(m.getDirector())
                            .pubDate(m.getPubDate())
                            .subtitle(m.getSubtitle())
                            .userRating(m.getUserRating())
                            .actor(m.getActor())
                            .build())
                    .collect(Collectors.toList());
        }
        static String deleteTagB(String str){
            String resultStr = str;
            resultStr = StringUtils.replace(resultStr, "<b>", "");
            resultStr = StringUtils.replace(resultStr, "</b>", "");
            return resultStr;
        }
    }
}
