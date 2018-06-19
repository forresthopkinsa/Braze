package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModConverter
import com.forresthopkinsa.braze.model.Mod
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

    fun add(mod: Mod): Mod {
        val entity = ModConverter.fromElement(mod)
        val saved = modRepo.save(entity)
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

}