package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.PackConverter
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

    fun addVersion(slug: String, version: IndexedPackVersion): Pack? = TODO()

    fun remove(slug: String): Int = db.deleteBySlug(slug)

    fun remove(slug: String, version: String): Boolean = TODO()

    fun getAll(): List<Pack> = db.findAll().map(PackConverter::fromEntity)

    fun getBySlug(slug: String): Pack? = db.findBySlug(slug).toElement()

    fun exists(slug: String): Boolean = db.existsById(slug)

    fun exists(slug: String, version: String): Boolean =
            getBySlug(slug)?.versions?.any { it.name == version } ?: false

    private fun PackConverter.PackEntity?.toElement(): Pack? = this?.toElement(PackConverter)

}