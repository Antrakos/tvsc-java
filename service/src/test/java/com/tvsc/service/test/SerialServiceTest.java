package com.tvsc.service.test;

import com.tvsc.core.AppProfiles;
import com.tvsc.core.model.Serial;
import com.tvsc.service.config.ServiceConfig;
import com.tvsc.service.services.SerialService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * @author Taras Zubrei
 */
@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceConfig.class)
@ActiveProfiles(AppProfiles.TEST)
public class SerialServiceTest {
    @Autowired
    private SerialService serialService;

    @Test
    public void whenGetSerialInfoCheckIfItHasOnlyGeneralInfo() {
        Serial serialInfo = serialService.getSerialInfo(78901L);
        assertThat(serialInfo, notNullValue());
        assertThat(serialInfo.getSeasons(), is(nullValue()));
    }

    @Test
    public void whenRestoreAllDataCheckIfDataPresents() {
        List<Serial> series = serialService.restoreAllData();
        assertThat(series, is(not(empty())));
    }
}
