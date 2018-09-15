import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.2.70"

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

    kotlin("jvm") version "1.2.70"
    kotlin("plugin.spring") version "1.2.70"
    kotlin("plugin.jpa") version "1.2.70"

    id("org.springframework.boot") version "2.0.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"

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
    compile("org.springframework.boot", "spring-boot-devtools")

    compile("org.springframework.security", "spring-security-web")
    compile("org.springframework.security", "spring-security-config")

    compile("com.google.code.gson", "gson")

    compile("com.h2database", "h2")

    testCompile("org.testng", "testng", "6.14.3")
    testCompile("org.springframework", "spring-test")
    testCompile("org.springframework.boot", "spring-boot-starter-test")
}

val bootJar by tasks.getting
val assemble by tasks.getting
val install by tasks.getting
val npm_run_dev by tasks.getting
val npm_run_build by tasks.getting

val rundev by tasks.creating {
    dependsOn(npm_run_dev)
}

val cleanStatic by tasks.creating(Delete::class) {
    delete("src/main/resources/public")
}

npm_run_build.dependsOn(cleanStatic)

bootJar.mustRunAfter(npm_run_build)

assemble.dependsOn(npm_run_build)

install.dependsOn(assemble)

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useTestNG()
}

node {
    nodeModulesDir = file("${project.projectDir}/src/frontend")
}

idea.module.inheritOutputDirs = true