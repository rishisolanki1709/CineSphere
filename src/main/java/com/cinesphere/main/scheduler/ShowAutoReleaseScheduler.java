package com.cinesphere.main.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cinesphere.main.entity.Show;
import com.cinesphere.main.entity.ShowStatus;
import com.cinesphere.main.repository.ShowRepository;

@Component
public class ShowAutoReleaseScheduler {

    @Autowired
    private ShowRepository showRepository;

    @Scheduled(fixedRate = 300000) // every 5 min
    public void expireShows() {

		List<Show> shows = showRepository.findAllActiveShows();

        for (Show show : shows) {
            if (show.getEndTime().isBefore(LocalDateTime.now())) {
                show.setStatus(ShowStatus.COMPLETED);
            }
        }

        showRepository.saveAll(shows);
    }
}
