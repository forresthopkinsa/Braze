package com.forresthopkinsa.braze.spring

import com.forresthopkinsa.braze.data.ModManager
import com.forresthopkinsa.braze.data.PackManager
import com.forresthopkinsa.braze.model.*
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/braze/api")
class RestController {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.info("Braze API controller initializing...")
    }

    @GetMapping("/go")
    fun go() = ModManager.add(SimpleMod(
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
    fun addMod(@RequestBody mod: SimpleMod): Mod = ModManager.add(mod)

    @PostMapping("/mods/{slug}")
    fun addModVersion(@PathVariable slug: String,
                      @RequestBody version: IndexedModVersion): Mod? = ModManager.addVersion(slug, version)

    @DeleteMapping("/mods/{slug}")
    fun deleteMod(@PathVariable slug: String): Boolean = ModManager.remove(slug) > 0

    @DeleteMapping("/mods/{slug}/{version}")
    fun deleteModVersion(@PathVariable slug: String,
                         @PathVariable version: String): Boolean = ModManager.remove(slug, version)

    @GetMapping("/packs")
    fun getPacks(): List<SimplePack> = PackManager.getAll().map(Pack::simplify)

    @GetMapping("/packs/{slug}")
    fun getPack(@PathVariable slug: String): Pack? = PackManager.getBySlug(slug)

    @GetMapping("/packs/{slug}/{version}")
    fun getPack(@PathVariable slug: String,
                @PathVariable version: String): IndexedPackVersion? =
            PackManager.getBySlug(slug)?.versions?.firstOrNull { it.name == version }

    @PostMapping("/packs")
    fun addPack(@RequestBody pack: SimplePack): Pack = PackManager.add(pack)

    @PostMapping("/packs/{slug}")
    fun addPackVersion(@PathVariable slug: String,
                       @RequestBody version: IndexedPackVersion): Pack? = PackManager.addVersion(slug, version)

    @DeleteMapping("/packs/{slug}")
    fun deletePack(@PathVariable slug: String): Boolean = PackManager.remove(slug) > 0

    @DeleteMapping("/packs/{slug}/{version}")
    fun deletePackVersion(@PathVariable slug: String,
                          @PathVariable version: String): Boolean = PackManager.remove(slug, version)

}