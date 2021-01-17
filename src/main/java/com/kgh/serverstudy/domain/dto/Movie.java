package com.kgh.serverstudy.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Getter
public class Movie implements Serializable {
    private String title;
    private String link;
    private float userRating;
}