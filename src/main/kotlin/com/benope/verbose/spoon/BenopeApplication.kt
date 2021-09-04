package com.benope.verbose.spoon;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class BenopeApplication

fun main(args: Array<String>) {
    runApplication<BenopeApplication>(*args)
}
