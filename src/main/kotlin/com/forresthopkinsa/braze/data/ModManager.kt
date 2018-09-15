package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModConverter
import com.forresthopkinsa.braze.model.DAO.ModConverter.ModEntity
import com.forresthopkinsa.braze.model.DAO.ModVersionConverter
import com.forresthopkinsa.braze.model.DAO.ModVersionConverter.ModVersionEntity
import com.forresthopkinsa.braze.model.IndexedModVersion
import com.forresthopkinsa.braze.model.Mod
import com.forresthopkinsa.braze.model.ModVersion
import com.forresthopkinsa.braze.model.SimpleMod
import org.springframework.stereotype.Component

@Component
class ModManager(override val db: ModRepository) :
    Manager<SimpleMod, Mod, ModEntity, ModVersionEntity, ModVersion, IndexedModVersion> {

    override val converter = ModConverter

    override val versionConverter = ModVersionConverter

    override fun ModEntity.setVersions(versions: List<ModVersionEntity>) = copy(versions = versions)

}