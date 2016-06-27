package com.tvsc.service.test;

import com.tvsc.core.AppProfiles;
import com.tvsc.core.model.Serial;
import com.tvsc.service.config.ServiceConfig;
import com.tvsc.service.services.SerialService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author Taras Zubrei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ServiceConfig.class)
@ActiveProfiles(AppProfiles.TEST)
public class SerialServiceTest {
    @Autowired
    private SerialService serialService;

    @Test
    public void getSerial() {
        serialService.getSerialInfo(78901L);
        Serial serial = serialService.getSerial(78901L);
        Assert.assertThat("Serial must not be null", serial, notNullValue());
    }

    @Test
    public void getAllData() {
        System.out.println(serialService.restoreAllData());
    }
}
