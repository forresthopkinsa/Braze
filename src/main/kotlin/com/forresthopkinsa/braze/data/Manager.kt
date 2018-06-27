package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.*
import com.forresthopkinsa.braze.model.Element
import com.forresthopkinsa.braze.model.IndexedVersion
import com.forresthopkinsa.braze.model.SimpleElement
import com.forresthopkinsa.braze.model.Version
import com.forresthopkinsa.braze.toElement

interface Manager<S : SimpleElement<S, E>,
        E : Element<E, S>,
        N : VersionedEntity<D>,
        D : DataEntity,
        V : Version,
        I : IndexedVersion<V>> {

    // Abstracts

    val db: DataRepository<N>

    val converter: EntityConverter<N, E>

    val versionConverter: EntityConverter<D, V>

    fun N.setVersions(versions: List<D>): N

    // Defaults

    fun add(simple: S): E = db.findBySlug(simple.slug).toElement()
            ?: converter.run {
                val entity = fromElement(simple.expand())
                return@run fromEntity(db.save(entity))
            }

    fun addVersion(slug: String, version: I): E? {
        if (!exists(slug)) return null
        if (exists(slug, version.name)) return getBySlug(slug)

        val entity = db.findBySlug(slug) ?: return null

        // if version is < 0 or > existing versions max, set as latest. Otherwise, insert where specified
        val versions = entity.versions.map(versionConverter::fromEntity).toMutableList()
        val index = version.index.takeUnless { it < 0 || it > versions.size } ?: versions.size
        versions.add(index, version.simplify())

        val new = entity.setVersions(versions.map(versionConverter::fromElement))

        val saved = db.save(new)
        return converter.fromEntity(saved)
    }

    fun remove(slug: String): Int = db.deleteBySlug(slug)

    fun remove(slug: String, version: String): Boolean {
        if (!exists(slug, version)) return false

        val entity = db.findBySlug(slug) ?: return false
        val versions = entity.versions.map(versionConverter::fromEntity).filterNot { it.name == version }
        val new = entity.setVersions(versions.map(versionConverter::fromElement))
        db.save(new)

        return (!exists(slug, version))
    }

    fun getAll(): List<E> = db.findAll().map(converter::fromEntity)

    fun getBySlug(slug: String): E? = db.findBySlug(slug).toElement()

    fun exists(slug: String): Boolean = db.existsById(slug)

    fun exists(slug: String, version: String): Boolean =
            getBySlug(slug)?.versions?.any { it.name == version } ?: false

    private fun N?.toElement(): E? = this?.toElement(converter)

}