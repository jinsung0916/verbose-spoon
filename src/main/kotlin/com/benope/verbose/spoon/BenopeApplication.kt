package com.benope.verbose.spoon;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class BenopeApplication : SpringBootServletInitializer()

fun main(args: Array<String>) {
    runApplication<BenopeApplication>(*args)
}