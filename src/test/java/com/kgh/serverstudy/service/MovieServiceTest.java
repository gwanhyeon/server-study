package com.kgh.serverstudy.service;
import com.kgh.serverstudy.config.NaverProperties;
import com.kgh.serverstudy.domain.dto.ResponseMovie;
import com.kgh.serverstudy.domain.repository.MovieRepository;
import com.kgh.serverstudy.domain.repository.MovieRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @Mock
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @Test
    @DisplayName("상위 2개 영화 평점 평균 계산")
    void 평균평점계산(){
        // given
        double expectedAvg = 5.450000047683716;
        Mockito.lenient().when(movieRepository.findByQuery(any())).thenReturn(getStubMovieList());
        movieService = new MovieService(movieRepository);
        // when
        double resultAvg = movieService.calAvgUserRating("test");
        assertEquals(expectedAvg, resultAvg);
    }

    @Test
    @DisplayName("<b>, </b> 태그 제거")
    void 태그제거_b(){

        //given
        int tagCount = 0;
        Mockito.lenient().when(movieRepository.findByQuery(any())).thenReturn(getStubMovieList());
        movieService = new MovieService(movieRepository);

        //when
        ResponseMovie.MovieDto movieDtoList = movieService.search("test");

        //then
        assertEquals(tagCount, StringUtils.countOccurrencesOf(movieDtoList.getItems().stream().findFirst().get().getTitle(),"<b>"));
        assertEquals(tagCount, StringUtils.countOccurrencesOf(movieDtoList.getItems().stream().findFirst().get().getTitle(),"</b>"));
    }

    @Test
    @DisplayName("평점이 0인 데이터 제외 여부")
    void 평점0_데이터제외(){

        // given
        int movieSize = 3;
        Mockito.lenient().when(movieRepository.findByQuery(any())).thenReturn(getStubMovieList());
        movieService = new MovieService(movieRepository);

        //when
        ResponseMovie.MovieDto movieDtoList = movieService.search("test");

        // then
        assertEquals(movieSize, movieDtoList.getItems().size());
    }

    @DisplayName("사용자 평점 순으로 정렬 Mockito")
    @Test
    void 평점순정렬_Mockito() {

        // given
        float rankingRate = 5.5f;
        Mockito.lenient().when(movieRepository.findByQuery(any())).thenReturn(getStubMovieList());
        MockMovieRepository mockMovieRepository = new MockMovieRepository(null, null);
        movieService =new MovieService(mockMovieRepository);

        // when
        ResponseMovie.MovieDto movieDto = movieService.search("쿼리");

        // then
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
        ResponseMovie.MovieDto movieDto = movieService.search("쿼리");

        // then
        assertEquals(rankingRate, movieDto.getItems().stream().findFirst().get().getUserRating());

   }
   private ResponseMovie.MovieDto getStubMovieList(){
       List<ResponseMovie.Item> ResponseMovieList = Arrays.asList(ResponseMovie.Item.builder()
                       .title("<b>ㅁ</b>")
                       .link("link-1")
                       .actor("actor-1")
                       .director("director-1")
                       .pubDate("pubdate-1")
                       .image("image-1")
                       .subtitle("subtitle-1")
                       .userRating(5.4f)
                       .build(),
               ResponseMovie.Item.builder()
                       .title("title-2")
                       .link("link-2")
                       .actor("actor-2")
                       .director("director-2")
                       .pubDate("pubdate-2")
                       .image("image-2")
                       .subtitle("subtitle-2")
                       .userRating(5.5f)
                       .build(),
               ResponseMovie.Item.builder()
                       .title("title-3")
                       .link("link-3")
                       .actor("actor-3")
                       .director("director-3")
                       .pubDate("pubdate-3")
                       .image("image-3")
                       .subtitle("subtitle-3")
                       .userRating(1.0f)
                       .build(),
               ResponseMovie.Item.builder()
                       .title("title-3")
                       .link("link-3")
                       .actor("actor-3")
                       .director("director-3")
                       .pubDate("pubdate-3")
                       .image("image-3")
                       .subtitle("subtitle-3")
                       .userRating(1.5f)
                       .build());
       return ResponseMovie.MovieDto.builder()
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
        public ResponseMovie.MovieDto findByQuery(String query) {
            List<ResponseMovie.Item> ResponseMovieList = Arrays.asList(ResponseMovie.Item.builder()
                            .title("title-1")
                            .link("link-1")
                            .actor("actor-1")
                            .director("director-1")
                            .pubDate("pubdate-1")
                            .image("image-1")
                            .subtitle("subtitle-1")
                            .userRating(5.4f)
                            .build(),
                    ResponseMovie.Item.builder()
                            .title("title-2")
                            .link("link-2")
                            .actor("actor-2")
                            .director("director-2")
                            .pubDate("pubdate-2")
                            .image("image-2")
                            .subtitle("subtitle-2")
                            .userRating(5.5f)
                            .build(),
                    ResponseMovie.Item.builder()
                            .title("title-3")
                            .link("link-3")
                            .actor("actor-3")
                            .director("director-3")
                            .pubDate("pubdate-3")
                            .image("image-3")
                            .subtitle("subtitle-3")
                            .userRating(0.0f)
                            .build(),
                    ResponseMovie.Item.builder()
                            .title("title-3")
                            .link("link-3")
                            .actor("actor-3")
                            .director("director-3")
                            .pubDate("pubdate-3")
                            .image("image-3")
                            .subtitle("subtitle-3")
                            .userRating(1.5f)
                            .build());
            return ResponseMovie.MovieDto.builder()
                    .start(1L)
                    .total(2L)
                    .display(3L)
                    .lastBuildDate("2021-01-01")
                    .items(ResponseMovieList)
                    .build();
        }
    }



}