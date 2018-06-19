package com.forresthopkinsa.braze.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan("com.forresthopkinsa.braze.spring", "com.forresthopkinsa.braze.data")
@EnableJpaRepositories(basePackages = ["com.forresthopkinsa.braze.data"])
@EntityScan("com.forresthopkinsa.braze.model")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

const val version = "0.1-SNAPSHOT"
const val name = "Braze"
const val stream = "DEV"