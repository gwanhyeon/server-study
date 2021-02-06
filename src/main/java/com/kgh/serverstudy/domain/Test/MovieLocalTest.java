package com.kgh.serverstudy.domain.Test;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class MovieLocalTest implements Serializable {
    private String title;
    private String link;
    private float userRating;
}
