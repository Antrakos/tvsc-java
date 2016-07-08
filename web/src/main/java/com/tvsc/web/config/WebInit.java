package com.tvsc.web.config;

import com.tvsc.core.AppProfiles;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Arrays;

/**
 * @author Taras Zubrei
 */
public class WebInit implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        container.setInitParameter("spring.profiles.default", AppProfiles.PRODUCTION);
        AnnotationConfigWebApplicationContext servletContext = new AnnotationConfigWebApplicationContext();

        ServletRegistration.Dynamic dispatcher = container.addServlet("spring-mvc-dispatcher", new DispatcherServlet(servletContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        dispatcher.setInitParameter("contextConfigLocation", "classpath*:WebMvcConfig.groovy");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        container.addFilter("corsFilter", new CorsFilter(source)).addMappingForUrlPatterns(null, false, "/*");
    }
}