package com.forresthopkinsa.braze.spring

import com.forresthopkinsa.braze.model.Solder.*
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
    fun getApi(): API = API(name, appVersion, stream)

    @GetMapping("/mod")
    fun getMod(): ModList = TODO()

    @GetMapping("/mod/{slug}")
    fun getMod(@PathVariable slug: String): Mod = TODO()

    @GetMapping("/mod/{slug}/{version}")
    fun getMod(@PathVariable slug: String, @PathVariable version: String): VersionedMod = TODO()

    @GetMapping("/modpack")
    fun getModpack(): ModpackList = TODO()

    @GetMapping("/modpack/{slug}")
    fun getModpack(@PathVariable slug: String): Modpack = TODO()

    @GetMapping("/modpack/{slug}/{build}")
    fun getModpack(@PathVariable slug: String, @PathVariable build: String): VersionedModpack = TODO()

}