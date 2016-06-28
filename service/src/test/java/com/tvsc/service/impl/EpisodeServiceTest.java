package com.tvsc.service.impl;

import com.tvsc.core.AppProfiles;
import com.tvsc.core.model.Episode;
import com.tvsc.persistence.repository.EpisodeRepository;
import com.tvsc.service.config.ServiceConfig;
import com.tvsc.service.utils.HttpUtils;
import com.tvsc.service.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Taras Zubrei
 */
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceConfig.class)
@ActiveProfiles(AppProfiles.TEST)
public class EpisodeServiceTest {
    @Autowired
    private EpisodeService episodeService;

    @Test
    public void whenGetTheEpisodesCheckIfItIsNotNull() {
        Episode episode = episodeService.getEpisode(5L);
        assertThat(episode, is(notNullValue()));
    }

    @Test
    public void whenGetAllEpisodesOfTheSerialCheckIfCollectionIsNotEmpty() {
        List<Episode> episodesOfSerial = episodeService.getEpisodesOfSerial(78901L);
        assertThat(episodesOfSerial, is(not(empty())));
    }

    @Nested
    @RunWith(JUnitPlatform.class)
    @ExtendWith(SpringExtension.class)
    @ContextConfiguration(classes = ServiceConfig.class)
    @ActiveProfiles(AppProfiles.TEST)
    class UnitTests {
        @Mock
        HttpUtils httpUtils;
        @Mock
        JsonUtils jsonUtils;
        @Mock
        UserService userService;
        @Mock
        EpisodeRepository episodeRepository;

        @InjectMocks
        private EpisodeService episodeService;
        private static final String json = "{\n" +
                "  \"data\": {\n" +
                "    \"id\": 8,\n" +
                "    \"airedSeason\": 1,\n" +
                "    \"airedEpisodeNumber\": 7,\n" +
                "    \"episodeName\": null,\n" +
                "    \"firstAired\": \"1997-04-14\",\n" +
                "    \"overview\": null,\n" +
                "    \"lastUpdated\": 1463747587,\n" +
                "    \"filename\": \"episodes/70327/8.jpg\",\n" +
                "    \"seriesId\": 70327,\n" +
                "    \"imdbId\": \"\",\n" +
                "    \"siteRating\": 7.4,\n" +
                "  },\n" +
                "  \"errors\": {\n" +
                "    \"invalidLanguage\": \"Some translations were not available in the specified language\"\n" +
                "  }\n" +
                "}";

        @BeforeEach
        public void setup() {
            episodeService = new EpisodeService();
            MockitoAnnotations.initMocks(this);
        }

        @Test
        public void getSingleEpisode() {
            Episode expected = new Episode();
            expected.setId(8L);
            expected.setFirstAired(LocalDate.parse("1997-04-14"));
            expected.setNumber(7);
            expected.setSeason(1);

            when(httpUtils.get(anyString())).thenReturn(json);
            when(jsonUtils.getSingleObject(anyString(), eq(Episode.class))).thenReturn(expected);

            Episode episode = episodeService.getEpisode(78901L);
            assertThat(episode, is(notNullValue()));
            assertThat(episode, is(equalTo(expected)));
        }
    }
}
