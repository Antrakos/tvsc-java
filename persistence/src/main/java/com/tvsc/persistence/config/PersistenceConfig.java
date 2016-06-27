package com.tvsc.persistence.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author Taras Zubrei
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.tvsc.persistence")
@PropertySource("classpath:persistence.properties")
public class PersistenceConfig {
    @Resource
    private Environment env;

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public DataSource productionDataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("db.prod.url"));
        dataSource.setUsername(env.getProperty("db.prod.username"));
        dataSource.setPassword(env.getProperty("db.prod.password"));
        dataSource.setDriverClassName(env.getProperty("db.prod.driver"));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
