package com.forresthopkinsa.braze.spring

import com.forresthopkinsa.braze.data.ModManager
import com.forresthopkinsa.braze.model.Mod
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/braze/api")
class RestController {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.info("Braze API controller initializing...")
    }

    @GetMapping("/mods") // todo: allow filtering
    fun getMods(): List<Mod> = ModManager.getAll()

    @GetMapping("/mods/{slug}") // todo: don't return a list here... probably rethink mod object design
    fun getMods(@PathVariable slug: String): List<Mod> = ModManager.getAll(slug)

    @GetMapping(value = ["/mods/{slug}/{version}"])
    fun getMod(@PathVariable slug: String, @PathVariable version: String): Mod = ModManager.get(slug, version)

}