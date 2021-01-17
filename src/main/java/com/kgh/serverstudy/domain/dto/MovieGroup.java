package com.kgh.serverstudy.domain.dto;

import java.util.List;
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
        return list.stream()
                .filter(b -> !((Float)b.getUserRating()).equals(0.0f))
                .sorted((a, b) -> b.getUserRating() > a.getUserRating() ? 1 : -1)
                .collect(Collectors.toList());
    }
}
