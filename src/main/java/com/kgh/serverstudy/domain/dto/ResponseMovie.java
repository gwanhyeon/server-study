package com.kgh.serverstudy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
        private DateFormat lastBuilderDate;
//        private LocalDate lastBuilderDate;
        private Long total;
        private Long start;
        private Long display;
        private List<ResponseMovie.Item> items;
        // todo field Adding

        public static ResponseMovie.MovieDto of(ResponseEntity<MovieDto> exchange, List<Item> listOrderRating){
            return ResponseMovie.MovieDto.builder()
                    .display(exchange.getBody().getDisplay())
                    .start(exchange.getBody().getStart())
                    .lastBuilderDate(exchange.getBody().getLastBuilderDate())
                    .total(exchange.getBody().getTotal())
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

        public static List<ResponseMovie.Item> of(ResponseEntity<MovieDto> exchange) {
            return exchange
                    .getBody()
                    .getItems()
                    .stream()
                    .map(m -> ResponseMovie.Item.builder()
                            .title(m.getTitle())
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
    }
}
