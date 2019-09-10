package com.moviedb.scheduler;

import com.moviedb.services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class MovieUpdate {
    private final MovieService movieService;
    private final Logger logger;

    @Autowired
    public MovieUpdate(MovieService movieService) {
        this.movieService = movieService;
        this.logger = LoggerFactory.getLogger(MovieUpdate.class);
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void doMovieUpdate(){
        logger.info("start scheduled movie");
        movieService.createOrUpdate();
        logger.info("end scheduled movie");
    }
}
