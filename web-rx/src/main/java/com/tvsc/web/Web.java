package com.tvsc.web;

import com.tvsc.service.config.ServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Antrakos
 */
@Import(ServiceConfig.class)
@SpringBootApplication
public class Web { //TODO: Reactor iterator appender collection error
    public static void main(String[] args) {
        SpringApplication.run(Web.class);
    }
}
