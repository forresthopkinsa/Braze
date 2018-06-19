package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModConverter
import com.forresthopkinsa.braze.model.Mod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
object ModManager {

    lateinit var db: ModRepository
        @Autowired
        set

    fun add(mod: Mod): Mod {
        val entity = ModConverter.fromDto(mod)
        val saved = db.save(entity)
        return ModConverter.fromDao(saved)
    }

    fun removeAll(slug: String): Int = db.deleteByParentSlug(slug)

    fun remove(slug: String, version: String): Int = db.deleteByParentSlugAndVersionName(slug, version)

    fun getAll(): List<Mod> = db.findAll().map(ModConverter::fromDao)

    fun getAll(slug: String): List<Mod> = db.findByParentSlug(slug).map(ModConverter::fromDao)

    fun get(slug: String, version: String): Mod {
        val entity = db.findByParentSlugAndVersionName(slug, version)
        return ModConverter.fromDao(entity)
    }

}