package com.tvsc.persistence;

import com.tvsc.core.AppProfiles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
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
public class Persistence {
    public static void main(String[] args) {
        SpringApplication.run(Persistence.class, args);
    }

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
