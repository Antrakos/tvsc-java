package com.tvsc.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Taras Zubrei
 */
@Configuration
@EnableWebMvc
@ImportResource("classpath*:WebConfig.groovy")
public class WebMvcConfig {
}
