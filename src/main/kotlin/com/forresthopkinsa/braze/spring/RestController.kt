package com.forresthopkinsa.braze.spring

import com.forresthopkinsa.braze.data.ModManager
import com.forresthopkinsa.braze.model.ForgeVersion
import com.forresthopkinsa.braze.model.Mod
import com.forresthopkinsa.braze.model.ModVersion
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/braze/api")
class RestController {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.info("Braze API controller initializing...")
    }

    @GetMapping("/go")
    fun go() = ModManager.add(Mod(
            slug = "slug",
            name = "name",
            author = "author",
            description = "description",
            link = null,
            donate = null,
            versions = listOf(ModVersion(
                    versionName = "version",
                    versionNumber = 123,
                    minForge = ForgeVersion.F1492,
                    maxForge = ForgeVersion.F1558,
                    md5 = null,
                    size = null,
                    dependencies = listOf()
            ))
    ))

    @GetMapping("/mods") // todo: allow filtering
    fun getMods(): List<Mod> = ModManager.getAll()

    @GetMapping("/mods/{slug}")
    fun getMods(@PathVariable slug: String): Mod = ModManager.getBySlug(slug)

    @GetMapping(value = ["/mods/{slug}/{version}"])
    fun getMod(@PathVariable slug: String, @PathVariable version: String): ModVersion? =
            ModManager.getBySlug(slug).versions.firstOrNull { it.versionName == version }

    @PostMapping("/mods")
    fun addMod(@RequestBody mod: Mod): Mod = ModManager.add(mod)

}