package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModConverter
import com.forresthopkinsa.braze.model.DAO.ModVersionConverter
import com.forresthopkinsa.braze.model.Mod
import com.forresthopkinsa.braze.model.ModVersion
import com.forresthopkinsa.braze.toElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
object ModManager {

    lateinit var modRepo: ModRepository
        @Autowired
        set

    /**
     * Any attached modversions are removed before saving
     */
    fun addMod(mod: Mod): Mod = modRepo.findBySlug(mod.slug).toElement()
            ?: ModConverter.run {
                val entity = fromElement(mod.copy(versions = listOf()))
                return@run fromEntity(modRepo.save(entity))
            }

    fun addVersion(slug: String, version: ModVersion): Mod? {
        if (!exists(slug)) return null // todo: http 404
        if (exists(slug, version.versionName)) return getBySlug(slug) // todo: http 409
        // todo: check if version number is taken, and if so, shift others up

        val versionEntity = ModVersionConverter.fromElement(version)

        val mod = modRepo.findBySlug(slug) ?: return null
        val new = mod.copy(versions = mod.versions + versionEntity)

        val saved = modRepo.save(new)
        return ModConverter.fromEntity(saved)
    }

    fun remove(slug: String): Int = modRepo.deleteBySlug(slug)

    fun remove(slug: String, version: String): Boolean {
        if (!exists(slug, version)) return false // todo: http 404

        val mod = modRepo.findBySlug(slug) ?: return false
        val new = mod.copy(versions = mod.versions.filterNot { it.versionName == version })
        modRepo.save(new)

        return (!exists(slug, version))
    }

    fun getAll(): List<Mod> = modRepo.findAll().map(ModConverter::fromEntity)

    fun getBySlug(slug: String): Mod? = modRepo.findBySlug(slug).toElement()

    fun exists(slug: String): Boolean = modRepo.existsById(slug)

    fun exists(slug: String, version: String): Boolean =
            getBySlug(slug)?.versions?.any { it.versionName == version } ?: false

    private fun ModConverter.ModEntity?.toElement(): Mod? = this?.toElement(ModConverter)
}