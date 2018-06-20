package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModConverter
import com.forresthopkinsa.braze.model.DAO.ModVersionConverter
import com.forresthopkinsa.braze.model.IndexedModVersion
import com.forresthopkinsa.braze.model.Mod
import com.forresthopkinsa.braze.model.SimpleMod
import com.forresthopkinsa.braze.toElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
object ModManager {

    lateinit var modRepo: ModRepository
        @Autowired
        set

    fun addMod(simpleMod: SimpleMod): Mod = modRepo.findBySlug(simpleMod.slug).toElement()
            ?: ModConverter.run {
                val entity = fromElement(simpleMod.expand())
                return@run fromEntity(modRepo.save(entity))
            }

    fun addVersion(slug: String, version: IndexedModVersion): Mod? {
        if (!exists(slug)) return null // todo: http 404
        if (exists(slug, version.name)) return getBySlug(slug) // todo: http 409

        val mod = modRepo.findBySlug(slug) ?: return null

        // if version is < 0 or > existing versions max, set as latest. Otherwise, insert where specified
        val versions = mod.versions.map(ModVersionConverter::fromEntity).toMutableList()
        val index = version.index.takeUnless { it < 0 || it > versions.size } ?: versions.size
        versions.add(index, version.simplify())

        val new = mod.copy(versions = versions.map(ModVersionConverter::fromElement))

        val saved = modRepo.save(new)
        return ModConverter.fromEntity(saved)
    }

    fun remove(slug: String): Int = modRepo.deleteBySlug(slug)

    fun remove(slug: String, version: String): Boolean {
        if (!exists(slug, version)) return false // todo: http 404

        val mod = modRepo.findBySlug(slug) ?: return false
        val new = mod.copy(versions = mod.versions.filterNot { it.name == version })
        modRepo.save(new)

        return (!exists(slug, version))
    }

    fun getAll(): List<Mod> = modRepo.findAll().map(ModConverter::fromEntity)

    fun getBySlug(slug: String): Mod? = modRepo.findBySlug(slug).toElement()

    fun exists(slug: String): Boolean = modRepo.existsById(slug)

    fun exists(slug: String, version: String): Boolean =
            getBySlug(slug)?.versions?.any { it.name == version } ?: false

    private fun ModConverter.ModEntity?.toElement(): Mod? = this?.toElement(ModConverter)
}