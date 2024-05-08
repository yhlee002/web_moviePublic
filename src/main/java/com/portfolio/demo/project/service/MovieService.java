package com.portfolio.demo.project.service;

import com.portfolio.demo.project.util.BoxOfficeListUtil;

import com.portfolio.demo.project.util.TMDBUtil;
import com.portfolio.demo.project.vo.tmdb.MovieDetailVO;
import com.portfolio.demo.project.vo.tmdb.MovieVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Slf4j
@Service
public class MovieService {

    private final BoxOfficeListUtil dailyBoxOfficeListUtil = new BoxOfficeListUtil();

    private final TMDBUtil tmdbUtil = new TMDBUtil();

    private final ResourceBundle bundle = ResourceBundle.getBundle("Res_ko_KR_keys");
    private final String KOBIS_KEY = bundle.getString("boxOfficeKey");
    private final String TMDB_KEY = bundle.getString("apiKey");
    private final String TMDB_ACCESS_TOKEN = bundle.getString("apiAccessToken");

    public static final String TMDB_IMAGE_PATH = "https://image.tmdb.org/t/p/"; // + file size / file path

    private LocalDateTime today = LocalDateTime.now();
    private String targetDt = today.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 일일 박스오피스
    private String minus1Dt = today.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 일일 박스오피스(전날)
    private int dayOfWeek = today.getDayOfWeek().getValue();
    private String weeklyTargetDt = today.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 주말 박스오피스
    private String weeklyTargetDt2 = today.minusDays(dayOfWeek).format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 주말 박스오피스(저번주)

    {
        dailyBoxOfficeListUtil.setKey(KOBIS_KEY);
        tmdbUtil.setKey(TMDB_KEY, TMDB_ACCESS_TOKEN);

        if (dayOfWeek < 5) {
            LocalDateTime minusDt = today.minusDays(dayOfWeek);
            weeklyTargetDt = minusDt.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
    }

    public List<com.portfolio.demo.project.vo.kobis.movie.MovieVO> getDailyBoxOfficeList() {
        List<com.portfolio.demo.project.vo.kobis.movie.MovieVO> list = dailyBoxOfficeListUtil.getDailyBoxOfficeMovies(targetDt);
        log.info("일간 박스오피스 기준일 : {}", targetDt);
        if (!list.isEmpty()) return list;
        else return dailyBoxOfficeListUtil.getDailyBoxOfficeMovies(minus1Dt);
    }

    public List<com.portfolio.demo.project.vo.kobis.movie.MovieVO> getWeeklyBoxOfficeList() {
        List<com.portfolio.demo.project.vo.kobis.movie.MovieVO> list = dailyBoxOfficeListUtil.getWeeklyBoxOfficeMovies(targetDt);
        log.info("주간 박스오피스 기준일 : {}", weeklyTargetDt);
        if (!list.isEmpty()) return list;
        else return dailyBoxOfficeListUtil.getWeeklyBoxOfficeMovies(weeklyTargetDt2);
    }

    public Map<String, Object> getMovieInfo(String movieCd) {
        return dailyBoxOfficeListUtil.getMovieInfo(movieCd);
    }

    /**
     * 단건 영화 정보 상세 조회
     *
     * @param movieId
     * @return
     */
    public MovieDetailVO getMovieDetail(String movieId) {
        return tmdbUtil.getMovieDetail(movieId);
    }

    /**
     * 영화 이미지 조회
     *
     * @param movieId
     * @return
     */
    public Map<String, Object> getMovieImages(String movieId) {
        return tmdbUtil.getMovieImages(movieId);
    }

    /**
     * 현재 상영중인 영화 조회
     *
     * @param page
     * @return
     */
    public List<MovieVO> getNowPlayingMovies(int page) {
        return tmdbUtil.getNowPlayingMovies(page);
    }

    /**
     * 최고 평점의 영화 조회
     *
     * @param page
     * @return
     */
    public List<MovieVO> getTopRatedMovies(int page) {
        return tmdbUtil.getTopRatedMovies(page);
    }

    /**
     * 인기 영화 조회
     *
     * @param page
     * @return
     */
    public List<MovieVO> getPopularMovies(int page) {
        return tmdbUtil.getPopularMovies(page);
    }

    /**
     * 개봉 예정인 영화 조회
     *
     * @param page
     * @return
     */
    public List<MovieVO> getUpComingMovies(int page) {
        return tmdbUtil.getUpcomingMovies(page);
    }

    /**
     * 영화 제목으로 검색
     *
     * @param query
     * @param includeAdult
     * @param page
     * @param year
     * @return
     */
    public List<MovieVO> getMovieListByTitle(String query, Boolean includeAdult, Integer page, String year) {
        return tmdbUtil.getMoviesByTitle(query, includeAdult, page, year);
    }
}
