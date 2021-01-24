package com.kgh.serverstudy.domain.dto;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class MovieGroup {
    private final List<ResponseMovie.Item> list;


    public MovieGroup(final List<ResponseMovie.Item> list) {
        this.list = list;
    }

    public List<ResponseMovie.Item> getList() {
        return list;
    }

    public List<ResponseMovie.Item> getListOrderRating() {
        // 평점이 0인 데이터는 제외하기
        return list.stream()
                .filter(b -> !((Float)b.getUserRating()).equals(0.0f))
                .sorted((a, b) -> b.getUserRating() > a.getUserRating() ? 1 : -1)
                .collect(Collectors.toList());
    }

    public OptionalDouble calAvgUserRating(){
        return getListOrderRating().stream()
                .limit(2)
                .mapToDouble(ResponseMovie.Item::getUserRating)
                .average();
    }


}
