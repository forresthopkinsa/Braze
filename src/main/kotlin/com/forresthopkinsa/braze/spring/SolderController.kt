package com.forresthopkinsa.braze.spring

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/solder/api")
class SolderController {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.info("Solder API controller initializing...")
    }

    @GetMapping("", "/")
    fun getApi() = "API endpoint"

    @GetMapping("/mod")
    fun getMod() = "Mod endpoint"

    @GetMapping("/mod/{slug}")
    fun getMod(@PathVariable slug: String) = "Mod endpoint: $slug"

    @GetMapping("/mod/{slug}/{version}")
    fun getMod(@PathVariable slug: String, @PathVariable version: String) = "Mod endpoint: $slug, $version"

    @GetMapping("/modpack")
    fun getModpack() = "Modpack endpoint"

    @GetMapping("/modpack/{slug}")
    fun getModpack(@PathVariable slug: String) = "Modpack endpoint: $slug"

    @GetMapping("/modpack/{slug}/{build}")
    fun getModpack(@PathVariable slug: String, @PathVariable build: String) = "Modpack endpoint: $slug, $build"

}