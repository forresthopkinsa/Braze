@file:Suppress("HasPlatformType", "PropertyName")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlinVersion: String by extra
    kotlinVersion = "1.3.21"

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

    kotlin("jvm") version "1.3.21"
    kotlin("plugin.spring") version "1.3.21"
    kotlin("plugin.jpa") version "1.3.21"

    id("org.springframework.boot") version "2.0.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"

    id("com.moowork.node") version "1.2.0"
}

val kotlinVersion: String by extra
val retrofitVersion = "2.4.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
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

    compile("com.github.ntrrgc", "ts-generator", "1.1.1")

    testCompile("org.testng", "testng", "6.14.3")
    testCompile("org.springframework", "spring-test")
    testCompile("org.springframework.boot", "spring-boot-starter-test")
}

// todo: replace with tasks{} block

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

val generateDefinitions by tasks.creating(JavaExec::class) {
    classpath = sourceSets["main"].runtimeClasspath
    main = "com.forresthopkinsa.braze.model.TypeDefGeneratorKt"
    standardOutput = File(buildDir, "braze-model.d.ts").outputStream()
}

val copyDefinitions by tasks.creating(Copy::class) {
    dependsOn(generateDefinitions)
    from(File(buildDir, "braze-model.d.ts"))
    destinationDir = File(projectDir, "src/frontend/src")
}

npm_run_build.dependsOn(cleanStatic)

bootJar.mustRunAfter(npm_run_build)

assemble.dependsOn(npm_run_build, copyDefinitions)

install.dependsOn(assemble)

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useTestNG()
}

springBoot {
    mainClassName = "com.forresthopkinsa.braze.spring.ApplicationKt"
}

node {
    nodeModulesDir = file("${project.projectDir}/src/frontend")
}

idea.module.inheritOutputDirs = true