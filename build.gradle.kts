import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.2.50"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
    }
}

group = "com.forresthopkinsa"
version = "0.1-SNAPSHOT"

plugins {
    java
    idea
    maven

    kotlin("jvm") version "1.2.50"
    kotlin("plugin.spring") version "1.2.50"

    id("org.springframework.boot") version "2.0.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.5.RELEASE"
}

val kotlinVersion: String by extra
val retrofitVersion = "2.4.0"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8", kotlinVersion))
    compile(kotlin("reflect", kotlinVersion))

    compile("org.springframework.boot", "spring-boot-starter")
    compile("org.springframework.boot", "spring-boot-starter-web")
    compile("org.springframework.boot", "spring-boot-starter-actuator")
    compile("org.springframework.boot", "spring-boot-starter-data-jpa")

    compile("com.google.code.gson", "gson")

    compile("com.h2database", "h2")
}

tasks {
    "install" {
        dependsOn("assemble")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}