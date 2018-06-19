package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO
import com.forresthopkinsa.braze.model.DAO.ModConverter
import com.forresthopkinsa.braze.model.Mod
import com.forresthopkinsa.braze.model.ModVersion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
object ModManager {

    lateinit var modRepo: ModRepository
        @Autowired
        set

    lateinit var versionRepo: ModVersionRepository
        @Autowired
        set

    /**
     * Any attached modversions are removed before saving
     */
    fun addMod(mod: Mod): Mod {
        if (exists(mod.slug)) return ModConverter.fromEntity(modRepo.findBySlug(mod.slug)) // todo: http 409

        val unversioned = mod.copy(versions = listOf())
        val entity = ModConverter.fromElement(unversioned)
        val saved = modRepo.save(entity)
        return ModConverter.fromEntity(saved)
    }

    fun addVersion(slug: String, version: ModVersion): Mod? {
        if (!exists(slug)) return null // todo: http 404
        if (exists(slug, version.versionName)) return getBySlug(slug) // todo: http 409
        // todo: check if version number is taken, and if so, shift others up

        val versionEntity = DAO.ModVersionConverter.fromElement(version)
        val mod = modRepo.findBySlug(slug)
        val new = mod.copy(versions = mod.versions + versionEntity)

        val saved = modRepo.save(new)
        return ModConverter.fromEntity(saved)
    }

    fun remove(slug: String): Int = modRepo.deleteBySlug(slug)

    fun remove(slug: String, version: String): Boolean {
        val versions = modRepo.findBySlug(slug).versions

        return versions.firstOrNull { it.versionName == version }?.let {
            versionRepo.delete(it)
            true
        } ?: false
    }

    fun getAll(): List<Mod> = modRepo.findAll().map(ModConverter::fromEntity)

    fun getBySlug(slug: String): Mod = ModConverter.fromEntity(modRepo.findBySlug(slug))

    fun exists(slug: String): Boolean = modRepo.existsById(slug)

    fun exists(slug: String, version: String) = getBySlug(slug).versions?.any { it.versionName == version } ?: false

}