package com.tvsc.service

import com.tvsc.persistence.Persistence
import com.tvsc.service.config.ServiceConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 *
 * @author Taras Zubrei
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("com.tvsc.service")
@Import(ServiceConfig::class, Persistence::class)
open class Service {
    companion object {
        @JvmStatic
        fun main(args: Array<String>): Unit {
            SpringApplication.run(Service::class.java, *args)
        }
    }
}