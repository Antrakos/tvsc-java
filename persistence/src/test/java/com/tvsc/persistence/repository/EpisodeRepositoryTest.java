package com.tvsc.persistence.repository;

import com.tvsc.persistence.config.PersistenceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Taras Zubrei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceConfig.class)
public class EpisodeRepositoryTest {
    @Autowired
    EpisodeRepository episodeRepository;
    private final Long USER_ID = 1L;
    private final List<Long> PRESENT_IDS = Arrays.asList(5378093L, 300428L, 312249L, 405398L, 3039311L);
    private final List<Long> ABSENT_IDS = Arrays.asList(4426795L, 5487129L, 362532L, 427739L);

    @Test
    public void getEpisodes() {
        List<Long> episodes = episodeRepository.getEpisodes(USER_ID, 78901L);
        assertThat(episodes, is(notNullValue()));
        assertThat(episodes.size(), is(equalTo(23)));
    }

    @Test
    public void setWatched() {
        episodeRepository.setWatched(USER_ID, 78901L, ABSENT_IDS);
        episodeRepository.setUnwatched(USER_ID, 78901L, ABSENT_IDS);
    }

    @Test(expected = DuplicateKeyException.class)
    public void setWatchedAlreadyWatchedEpisodeExpectedDuplicatedKey() {
        episodeRepository.setWatched(USER_ID, 78901L, PRESENT_IDS);
    }

    @Test(expected = RuntimeException.class)
    public void setUnwatchedYetNotWatchedEpisodeExpectedDuplicatedKey() {
        episodeRepository.setUnwatched(USER_ID, 78901L, ABSENT_IDS);
    }
}
