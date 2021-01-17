package com.kgh.serverstudy.domain.repository;
import com.kgh.serverstudy.domain.dto.ResponseMovie;

import java.util.List;

public interface MovieRepository {
    List<ResponseMovie.Item> findByQuery(String query);
}
