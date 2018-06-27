package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.PackConverter
import com.forresthopkinsa.braze.model.DAO.PackVersionConverter
import com.forresthopkinsa.braze.model.IndexedPackVersion
import com.forresthopkinsa.braze.model.Pack
import com.forresthopkinsa.braze.model.SimplePack
import com.forresthopkinsa.braze.toElement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
object PackManager {

    lateinit var db: PackRepository
        @Autowired
        set

    fun addPack(simplePack: SimplePack): Pack = db.findBySlug(simplePack.slug).toElement()
            ?: PackConverter.run {
                val entity = fromElement(simplePack.expand())
                return@run fromEntity(db.save(entity))
            }

    fun addVersion(slug: String, version: IndexedPackVersion): Pack? {
        if (!exists(slug)) return null
        if (exists(slug, version.name)) return getBySlug(slug)

        val pack = db.findBySlug(slug) ?: return null

        // if version is < 0 or > existing versions max, set as latest. Otherwise, insert where specified
        val versions = pack.versions.map(PackVersionConverter::fromEntity).toMutableList()
        val index = version.index.takeUnless { it < 0 || it > versions.size } ?: versions.size
        versions.add(index, version.simplify())

        val new = pack.copy(versions = versions.map(PackVersionConverter::fromElement))

        val saved = db.save(new)
        return PackConverter.fromEntity(saved)
    }

    fun remove(slug: String): Int = db.deleteBySlug(slug)

    fun remove(slug: String, version: String): Boolean = TODO()

    fun getAll(): List<Pack> = db.findAll().map(PackConverter::fromEntity)

    fun getBySlug(slug: String): Pack? = db.findBySlug(slug).toElement()

    fun exists(slug: String): Boolean = db.existsById(slug)

    fun exists(slug: String, version: String): Boolean =
            getBySlug(slug)?.versions?.any { it.name == version } ?: false

    private fun PackConverter.PackEntity?.toElement(): Pack? = this?.toElement(PackConverter)

}