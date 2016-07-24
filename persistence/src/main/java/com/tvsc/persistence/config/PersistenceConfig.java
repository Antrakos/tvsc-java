package com.tvsc.persistence.config;

import com.tvsc.core.AppProfiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * @author Taras Zubrei
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.tvsc.persistence")
@PropertySource("classpath:persistence/application.properties")
public class PersistenceConfig {

    @Value("${db.schema}")
    String schema;
    @Value("${db.data}")
    String data;

    @Bean
    @Profile(AppProfiles.TEST)
    public DataSource testDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScripts(schema, data)
                .build();
    }
}
