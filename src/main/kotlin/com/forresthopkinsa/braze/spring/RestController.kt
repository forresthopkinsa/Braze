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
    fun go() = ModManager.addMod(Mod(
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
    fun getMods(): List<Mod> = ModManager.getAll().map { it.copy(versions = null) }

    @GetMapping("/mods/{slug}")
    fun getMod(@PathVariable slug: String): Mod? = ModManager.getBySlug(slug) // todo: http 404

    @GetMapping(value = ["/mods/{slug}/{version}"])
    fun getMod(@PathVariable slug: String,
               @PathVariable version: String): ModVersion? =
            ModManager.getBySlug(slug)?.versions?.firstOrNull { it.versionName == version }

    @PostMapping("/mods")
    fun addMod(@RequestBody mod: Mod): Mod = ModManager.addMod(mod)

    @PostMapping("/mods/{slug}")
    fun addVersion(@PathVariable slug: String,
                   @RequestBody version: ModVersion): Mod? = ModManager.addVersion(slug, version)

    @DeleteMapping("/mods/{slug}")
    fun deleteMod(@PathVariable slug: String): Boolean = ModManager.remove(slug) > 0

    @DeleteMapping("/mods/{slug}/{version}")
    fun deleteVersion(@PathVariable slug: String,
                      @PathVariable version: String): Boolean = ModManager.remove(slug, version)

}