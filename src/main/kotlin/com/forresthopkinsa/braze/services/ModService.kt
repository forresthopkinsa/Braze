package com.forresthopkinsa.braze.services

import com.forresthopkinsa.braze.data.ModManager
import com.forresthopkinsa.braze.model.IndexedModVersion
import com.forresthopkinsa.braze.model.Mod
import com.forresthopkinsa.braze.model.SimpleMod
import com.forresthopkinsa.braze.spring.InvalidSlugException
import com.forresthopkinsa.braze.spring.InvalidVersionException
import com.forresthopkinsa.braze.spring.ModNotFoundException
import com.forresthopkinsa.braze.spring.ModVersionNotFoundException
import org.springframework.stereotype.Service

/**
 * Mostly for the sake of input validation and type conversion
 */
@Service
class ModService(private val modManager: ModManager) {

    fun getSimpleMods() = modManager.getAll().map(Mod::simplify)

    fun getBySlug(slug: String) = modManager.getBySlug(slug) ?: throw ModNotFoundException(slug)

    fun getIndexedVersion(slug: String, version: String) =
        getBySlug(slug).versions.firstOrNull { it.name == version }
            ?: throw ModVersionNotFoundException(slug, version)

    fun addSimpleMod(mod: SimpleMod): Mod {
        if (!validateMod(mod.slug)) throw InvalidSlugException(mod.slug)

        return modManager.add(mod)
    }

    fun addIndexedVersion(slug: String, version: IndexedModVersion): Mod {
        if (!validateMod(slug)) throw InvalidSlugException(slug)
        if (!validateVersion(version.name)) throw InvalidVersionException(version.name)

        return modManager.addVersion(slug, version) ?: throw ModNotFoundException(slug)
    }

    fun deleteBySlug(slug: String) = modManager.remove(slug) > 0

    fun deleteVersion(slug: String, version: String) = modManager.remove(slug, version)

    private fun validateMod(string: String) =
        string.length >= 3 && !string.contains("[^A-Za-z0-9\\-]".toRegex())

    private fun validateVersion(string: String) =
        string.isNotBlank() && !string.contains("[^A-Za-z0-9\\-.]".toRegex())

}