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
    kotlin("plugin.jpa") version "1.2.50"

    id("org.springframework.boot") version "2.0.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.5.RELEASE"

    id("com.moowork.node") version "1.2.0"
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
    "rundev" {
        dependsOn("npm_run_dev") // better to run this directly; gradle doesn't shutdown the server correctly
    }

    "npm_run_build" {
        dependsOn("npm_install")
    }

    "assemble" {
        dependsOn("npm_run_build")
    }

    "install" {
        dependsOn("assemble")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

node {
    nodeModulesDir = file("${project.projectDir}/src/frontend")
}

idea.module.inheritOutputDirs = true