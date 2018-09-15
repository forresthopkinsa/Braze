package com.forresthopkinsa.braze.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan("com.forresthopkinsa.braze")
@EnableJpaRepositories(basePackages = ["com.forresthopkinsa.braze.data"])
@EntityScan("com.forresthopkinsa.braze.model")
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

const val APP_VERSION = "0.1-SNAPSHOT"
const val APP_NAME = "Braze"
const val APP_STREAM = "DEV"