package com.kgh.serverstudy.service;
import com.kgh.serverstudy.config.NaverConfig.NaverProperties;
import com.kgh.serverstudy.domain.dto.Movie;
import com.kgh.serverstudy.domain.repository.MovieRepository;
import com.kgh.serverstudy.domain.repository.MovieRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @Mock
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @Test
    @DisplayName("상위 2개 영화 평점 평균 계산")
    void 평균평점계산(){

        //given
        double expectedAvg = 5.450000047683716;
        when(movieRepository.findByQuery(any())).thenReturn(getStubMovieList());
        movieService = new MovieService(movieRepository);

        //when
        double resultAvg = movieService.calAvgUserRating("test");

        //then
        assertEquals(expectedAvg, resultAvg);
    }

    @Test
    @DisplayName("<b>, </b> 태그 제거")
    void 태그제거_b(){

        //given
        int tagCount = 0;
        when(movieRepository.findByQuery(any())).thenReturn(getStubMovieList());
        movieService = new MovieService(movieRepository);

        //when
        Movie.MovieDto movieDtoList = movieService.findByQuery("test");

        //then
        assertEquals(tagCount, StringUtils.countOccurrencesOf(movieDtoList.getItems().stream().findFirst().get().getTitle(),"<b>"));
        assertEquals(tagCount, StringUtils.countOccurrencesOf(movieDtoList.getItems().stream().findFirst().get().getTitle(),"</b>"));
    }

    @Test
    @DisplayName("평점이 0인 데이터 제외 여부")
    void 평점0_데이터제외(){

        //given
        int movieSize = 4;
        when(movieRepository.findByQuery(any())).thenReturn(getStubMovieList());
        movieService = new MovieService(movieRepository);

        //when
        Movie.MovieDto movieDtoList = movieService.findByQuery("test");

        //then
        assertEquals(movieSize, movieDtoList.getItems().size());
    }

    @DisplayName("사용자 평점 순으로 정렬 Mockito")
    @Test
    void 평점순정렬_Mockito() {

        //given
        float rankingRate = 5.4f;
        //when(movieRepository.findByQuery(any())).thenReturn(getStubMovieList());
        MockMovieRepository mockMovieRepository = new MockMovieRepository(null, null);
        movieService =new MovieService(mockMovieRepository);

        //when
        Movie.MovieDto movieDto = movieService.findByQuery("쿼리");

        //then
        assertEquals(rankingRate, movieDto.getItems().stream().findFirst().get().getUserRating());
    }

    @DisplayName("사용자 평점 순으로 정렬")
    @Test
    void 평점순정렬() {

        // given
        float rankingRate = 5.4f;
        MockMovieRepository mockMovieRepository = new MockMovieRepository(null, null);
        movieService =new MovieService(mockMovieRepository);

        // when
        Movie.MovieDto movieDto = movieService.findByQuery("쿼리");

        // then
        assertEquals(rankingRate, movieDto.getItems().stream().findFirst().get().getUserRating());
   }


    @Test
    void 영화_캐시_조회() {

        //given
        float rankingScore = 5.4f;
        when(movieRepository.findCacheByQuery(any())).thenReturn(getStubMovieMapList());
        movieService = new MovieService(movieRepository);

        // when
        Map<String, Movie.MovieDto> query = movieService.findCacheByQuery("Query");

        // then
        assertEquals(rankingScore, query.get("Query").getItems().stream().findFirst().get().getUserRating());

    }

    @Test
    void 강제_캐시_업데이트() throws InterruptedException {
        float rankingScore = 5.4f;
        // given
        when(movieRepository.findCacheByQuery(any())).thenReturn(getStubMovieMapList());
        movieService = new MovieService(movieRepository);

        // when
        Map<String, Movie.MovieDto> nextMapList = new HashMap<>();
        Map<String, Movie.MovieDto> prevMapList = movieService.findCacheByQuery("Query");
        long nowTime = System.currentTimeMillis();
        long afterTime = nowTime;

        // wait 10 minute
        Thread t = new Thread(String.valueOf(prevMapList));
        t.start();
        t.join(600000);

        if(!CollectionUtils.isEmpty(prevMapList)) {
            nextMapList = movieService.saveCacheByQuery("Query");
            afterTime =0L;
        }

        // then
        assertTrue(!t.isAlive());
        assertNotEquals(nowTime, afterTime);
        assertEquals(new HashMap<>(), nextMapList);


    }

    private Map<String, Movie.MovieDto> getStubMovieMapList(){
        List<Movie.Item> ResponseMovieList = Arrays.asList(Movie.Item.builder()
                        .title("<b>ㅁ</b>")
                        .link("link-1")
                        .actor("actor-1")
                        .director("director-1")
                        .pubDate("pubdate-1")
                        .image("image-1")
                        .subtitle("subtitle-1")
                        .userRating(5.4f)
                        .build(),
                Movie.Item.builder()
                        .title("title-2")
                        .link("link-2")
                        .actor("actor-2")
                        .director("director-2")
                        .pubDate("pubdate-2")
                        .image("image-2")
                        .subtitle("subtitle-2")
                        .userRating(5.5f)
                        .build(),
                Movie.Item.builder()
                        .title("title-3")
                        .link("link-3")
                        .actor("actor-3")
                        .director("director-3")
                        .pubDate("pubdate-3")
                        .image("image-3")
                        .subtitle("subtitle-3")
                        .userRating(1.0f)
                        .build(),
                Movie.Item.builder()
                        .title("title-3")
                        .link("link-3")
                        .actor("actor-3")
                        .director("director-3")
                        .pubDate("pubdate-3")
                        .image("image-3")
                        .subtitle("subtitle-3")
                        .userRating(1.5f)
                        .build());
        Movie.MovieDto mapList = Movie.MovieDto.builder()
                .start(1L)
                .total(2L)
                .display(3L)
                .lastBuildDate("2021-01-01")
                .items(ResponseMovieList)
                .build();

        Map<String, Movie.MovieDto> query = new HashMap<>();
        query.put("Query", mapList);
        return query;

    }
    private Movie.MovieDto getStubMovieList(){
       List<Movie.Item> ResponseMovieList = Arrays.asList(Movie.Item.builder()
                       .title("<b>ㅁ</b>")
                       .link("link-1")
                       .actor("actor-1")
                       .director("director-1")
                       .pubDate("pubdate-1")
                       .image("image-1")
                       .subtitle("subtitle-1")
                       .userRating(5.4f)
                       .build(),
               Movie.Item.builder()
                       .title("title-2")
                       .link("link-2")
                       .actor("actor-2")
                       .director("director-2")
                       .pubDate("pubdate-2")
                       .image("image-2")
                       .subtitle("subtitle-2")
                       .userRating(5.5f)
                       .build(),
               Movie.Item.builder()
                       .title("title-3")
                       .link("link-3")
                       .actor("actor-3")
                       .director("director-3")
                       .pubDate("pubdate-3")
                       .image("image-3")
                       .subtitle("subtitle-3")
                       .userRating(1.0f)
                       .build(),
               Movie.Item.builder()
                       .title("title-3")
                       .link("link-3")
                       .actor("actor-3")
                       .director("director-3")
                       .pubDate("pubdate-3")
                       .image("image-3")
                       .subtitle("subtitle-3")
                       .userRating(1.5f)
                       .build());
       return Movie.MovieDto.builder()
               .start(1L)
               .total(2L)
               .display(3L)
               .lastBuildDate("2021-01-01")
               .items(ResponseMovieList)
               .build();
   }


    static class MockMovieRepository extends MovieRepositoryImpl {
        public MockMovieRepository(NaverProperties naverProperties, RestTemplate restTemplate) {
            super(naverProperties, restTemplate);
        }

        @Override
        public Movie.MovieDto findByQuery(String query) {
            List<Movie.Item> ResponseMovieList = Arrays.asList(Movie.Item.builder()
                            .title("title-1")
                            .link("link-1")
                            .actor("actor-1")
                            .director("director-1")
                            .pubDate("pubdate-1")
                            .image("image-1")
                            .subtitle("subtitle-1")
                            .userRating(5.4f)
                            .build(),
                    Movie.Item.builder()
                            .title("title-2")
                            .link("link-2")
                            .actor("actor-2")
                            .director("director-2")
                            .pubDate("pubdate-2")
                            .image("image-2")
                            .subtitle("subtitle-2")
                            .userRating(5.5f)
                            .build(),
                    Movie.Item.builder()
                            .title("title-3")
                            .link("link-3")
                            .actor("actor-3")
                            .director("director-3")
                            .pubDate("pubdate-3")
                            .image("image-3")
                            .subtitle("subtitle-3")
                            .userRating(0.0f)
                            .build(),
                    Movie.Item.builder()
                            .title("title-3")
                            .link("link-3")
                            .actor("actor-3")
                            .director("director-3")
                            .pubDate("pubdate-3")
                            .image("image-3")
                            .subtitle("subtitle-3")
                            .userRating(1.5f)
                            .build());
            return Movie.MovieDto.builder()
                    .start(1L)
                    .total(2L)
                    .display(3L)
                    .lastBuildDate("2021-01-01")
                    .items(ResponseMovieList)
                    .build();
        }
    }



}