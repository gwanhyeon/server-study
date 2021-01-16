package com.kgh.serverstudy.Repository;

import com.kgh.serverstudy.domain.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> findByQuery(String query);
}
