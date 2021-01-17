package com.kgh.serverstudy.domain.repository;
import com.kgh.serverstudy.domain.dto.Movie;
import java.util.List;

public interface MovieRepository {
    List<Movie> findByQuery(String query);
}
