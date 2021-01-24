package com.kgh.serverstudy.domain.dto;

import com.kgh.serverstudy.config.strUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

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
    private strUtils strUtils = new strUtils();

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MovieDto {
        private String lastBuildDate;
        private Long total;
        private Long start;
        private Long display;
        private List<ResponseMovie.Item> items;
        // todo field Adding

        public static ResponseMovie.MovieDto of(ResponseEntity<MovieDto> exchange, List<Item> listOrderRating){
            return ResponseMovie.MovieDto.builder()
                    .display(exchange.getBody().getDisplay())
                    .start(exchange.getBody().getStart())
                    .lastBuildDate(exchange.getBody().getLastBuildDate())
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


        public String getTitle() {
            return deleteTagB(title);
        }

        public static List<ResponseMovie.Item> of(ResponseEntity<MovieDto> exchange) {
            return exchange
                    .getBody()
                    .getItems()
                    .stream()
                    .map(m -> ResponseMovie.Item.builder()
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
