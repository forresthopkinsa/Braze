package com.forresthopkinsa.braze.spring

import com.forresthopkinsa.braze.data.ModManager
import com.forresthopkinsa.braze.model.IndexedModVersion
import com.forresthopkinsa.braze.model.Mod
import com.forresthopkinsa.braze.model.SimpleMod
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
    fun go() = ModManager.addMod(SimpleMod(
            slug = "slug",
            name = "name",
            author = "author",
            description = "description",
            link = null,
            donate = null
    ))

    @GetMapping("/mods") // todo: allow filtering
    fun getMods(): List<SimpleMod> = ModManager.getAll().map(Mod::simplify)

    @GetMapping("/mods/{slug}")
    fun getMod(@PathVariable slug: String): Mod? = ModManager.getBySlug(slug) // todo: http 404

    @GetMapping(value = ["/mods/{slug}/{version}"])
    fun getMod(@PathVariable slug: String,
               @PathVariable version: String): IndexedModVersion? =
            ModManager.getBySlug(slug)?.versions?.firstOrNull { it.name == version }

    @PostMapping("/mods")
    fun addMod(@RequestBody mod: SimpleMod): Mod = ModManager.addMod(mod)

    @PostMapping("/mods/{slug}")
    fun addVersion(@PathVariable slug: String,
                   @RequestBody version: IndexedModVersion): Mod? = ModManager.addVersion(slug, version)

    @DeleteMapping("/mods/{slug}")
    fun deleteMod(@PathVariable slug: String): Boolean = ModManager.remove(slug) > 0

    @DeleteMapping("/mods/{slug}/{version}")
    fun deleteVersion(@PathVariable slug: String,
                      @PathVariable version: String): Boolean = ModManager.remove(slug, version)

}