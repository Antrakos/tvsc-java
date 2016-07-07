package com.tvsc.web.config

import com.tvsc.core.AppProfiles
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

import javax.servlet.ServletContext
import javax.servlet.ServletException

/**
 *
 * @author Taras Zubrei
 */
class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() { [WebConfig] }

    @Override
    protected Class<?>[] getServletConfigClasses() { [] }

    @Override
    protected String[] getServletMappings() { ['/'] }

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        container.setInitParameter("spring.profiles.default", AppProfiles.PRODUCTION)
        def servletContext = new AnnotationConfigWebApplicationContext()

        def dispatcher = container.addServlet("spring-mvc-dispatcher", new DispatcherServlet(servletContext))
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping("/")

        def config = new CorsConfiguration(allowCredentials: true,
                allowedOrigins: ['*'], allowedHeaders: ['*'], allowedMethods: ['*']);
        def source = new UrlBasedCorsConfigurationSource(corsConfigurations: ['/**': config]);
        container.addFilter('corsFilter', new CorsFilter(source)).addMappingForUrlPatterns(null, false, "/*");

        super.onStartup(container);
    }
}