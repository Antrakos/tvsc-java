package com.tvsc.persistence.repository

import com.tvsc.core.AppProfiles
import com.tvsc.core.exception.ApplicationException
import com.tvsc.persistence.config.PersistenceConfig
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.BatchUpdateException
import java.util.*

/**
 * @author Taras Zubrei
 */
@RunWith(JUnitPlatform::class)
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = arrayOf(PersistenceConfig::class))
@ActiveProfiles(AppProfiles.TEST)
class EpisodeRepositoryTest {
    @Autowired
    internal var episodeRepository: EpisodeRepository? = null

    private val USER_ID = 1L
    private val PRESENT_IDS = Arrays.asList(5378093L, 300428L, 312249L, 405398L, 3039311L)
    private val ABSENT_IDS = Arrays.asList(4426795L, 5487129L, 362532L, 427739L)

    @Test
    fun getEpisodes() {
        val episodes = episodeRepository?.getEpisodes(USER_ID, 78901L)
        assertThat(episodes, `is`(notNullValue()))
        assertThat(episodes?.size, `is`(equalTo(19)))
    }

    @Test
    fun setWatched() {
        episodeRepository?.setWatched(USER_ID, 78901L, ABSENT_IDS)
        episodeRepository?.setUnwatched(USER_ID, 78901L, ABSENT_IDS)
    }

    @Test
    fun setWatchedAlreadyWatchedEpisodeExpectedDuplicatedKey() {
        Assertions.expectThrows(DuplicateKeyException::class.java) { episodeRepository?.setWatched(USER_ID, 78901L, PRESENT_IDS) }
    }

    @Test
    fun setUnwatchedYetNotWatchedEpisodeExpectedDuplicatedKey() {
        val exception = Assertions.expectThrows(ApplicationException::class.java) { episodeRepository?.setUnwatched(USER_ID, 78901L, ABSENT_IDS) }
        assertThat(exception.cause, `is`(instanceOf(BatchUpdateException::class.java)))
    }
}
