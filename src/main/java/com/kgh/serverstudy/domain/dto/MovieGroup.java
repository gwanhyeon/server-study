package com.kgh.serverstudy.domain.dto;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class MovieGroup {
    private final List<Movie.Item> list;
    private final static int AVG_TOP_NUM = 2;

    public MovieGroup(final List<Movie.Item> list) {
        this.list = list;
    }

    public List<Movie.Item> getList() {
        return list;
    }

    public List<Movie.Item> getListOrderRating() {
        // 평점이 0인 데이터는 제외하기
        return list.stream()
                .filter(b -> !((Float)b.getUserRating()).equals(0.0f))
                .sorted((a, b) -> b.getUserRating() > a.getUserRating() ? 1 : -1)
                .collect(Collectors.toList());
    }
    public OptionalDouble calAvgUserRating(){
        return getListOrderRating().stream()
                .limit(AVG_TOP_NUM)
                .mapToDouble(Movie.Item::getUserRating)
                .average();
    }
}
