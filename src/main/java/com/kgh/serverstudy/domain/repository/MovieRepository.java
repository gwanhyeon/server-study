package com.kgh.serverstudy.domain.repository;
import com.kgh.serverstudy.domain.dto.ResponseMovie;

import java.util.List;

public interface MovieRepository {
    ResponseMovie.MovieDto findByQuery(String query);
    ResponseMovie.MovieDto findByOrderQuery(String query);
}
