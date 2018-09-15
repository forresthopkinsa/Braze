package com.forresthopkinsa.braze.spring

import com.forresthopkinsa.braze.data.ModManager
import com.forresthopkinsa.braze.data.PackManager
import com.forresthopkinsa.braze.model.*
import com.forresthopkinsa.braze.services.ModService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
@RequestMapping("/braze/api")
class RestController(
    private val modManager: ModManager,
    private val packManager: PackManager,
    private val modService: ModService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    init {
        log.info("Braze API controller initializing...")
    }

    class Constants(
        val forge: List<ForgeVersion.ForgeConstant> = ForgeVersion.constants(),
        val game: List<GameVersion.GameConstant> = GameVersion.constants(),
        val java: List<JavaVersion.JavaConstant> = JavaVersion.constants()
    )

    @GetMapping("/go")
    fun go() = modManager.add(
        SimpleMod(
            slug = "slug",
            name = "name",
            author = "author",
            description = "description",
            link = null,
            donate = null
        )
    )

    @GetMapping("/constants")
    fun getConstants() = Constants()

    @GetMapping("/mods") // todo: allow filtering
    fun getMods(): List<SimpleMod> = modService.getSimpleMods()

    @GetMapping("/mods/{slug}")
    fun getMod(@PathVariable slug: String): Mod? = modService.getBySlug(slug)

    @GetMapping("/mods/{slug}/{version}")
    fun getMod(
        @PathVariable slug: String,
        @PathVariable version: String
    ): IndexedModVersion? = modService.getIndexedVersion(slug, version)

    @PostMapping("/mods")
    fun addMod(@RequestBody mod: SimpleMod): Mod = modService.addSimpleMod(mod)

    @PostMapping("/mods/{slug}")
    fun addModVersion(
        @PathVariable slug: String,
        @RequestBody version: IndexedModVersion
    ): Mod? = modService.addIndexedVersion(slug, version)

    @DeleteMapping("/mods/{slug}")
    fun deleteMod(@PathVariable slug: String): Boolean = modService.deleteBySlug(slug)

    @DeleteMapping("/mods/{slug}/{version}")
    fun deleteModVersion(
        @PathVariable slug: String,
        @PathVariable version: String
    ): Boolean = modService.deleteVersion(slug, version)

    @GetMapping("/packs")
    fun getPacks(): List<SimplePack> = packManager.getAll().map(Pack::simplify)

    @GetMapping("/packs/{slug}")
    fun getPack(@PathVariable slug: String): Pack? = packManager.getBySlug(slug)

    @GetMapping("/packs/{slug}/{version}")
    fun getPack(
        @PathVariable slug: String,
        @PathVariable version: String
    ): IndexedPackVersion? =
        packManager.getBySlug(slug)?.versions?.firstOrNull { it.name == version }

    @PostMapping("/packs")
    fun addPack(@RequestBody pack: SimplePack): Pack = packManager.add(pack)

    @PostMapping("/packs/{slug}")
    fun addPackVersion(
        @PathVariable slug: String,
        @RequestBody version: IndexedPackVersion
    ): Pack? = packManager.addVersion(slug, version)

    @DeleteMapping("/packs/{slug}")
    fun deletePack(@PathVariable slug: String): Boolean = packManager.remove(slug) > 0

    @DeleteMapping("/packs/{slug}/{version}")
    fun deletePackVersion(
        @PathVariable slug: String,
        @PathVariable version: String
    ): Boolean = packManager.remove(slug, version)

}