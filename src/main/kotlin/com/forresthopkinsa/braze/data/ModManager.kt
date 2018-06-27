package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModConverter
import com.forresthopkinsa.braze.model.DAO.ModConverter.ModEntity
import com.forresthopkinsa.braze.model.DAO.ModVersionConverter
import com.forresthopkinsa.braze.model.DAO.ModVersionConverter.ModVersionEntity
import com.forresthopkinsa.braze.model.IndexedModVersion
import com.forresthopkinsa.braze.model.Mod
import com.forresthopkinsa.braze.model.ModVersion
import com.forresthopkinsa.braze.model.SimpleMod
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
object ModManager : Manager<SimpleMod, Mod, ModEntity, ModVersionEntity, ModVersion, IndexedModVersion> {

    override lateinit var db: ModRepository
        @Autowired
        set

    override val converter = ModConverter

    override val versionConverter = ModVersionConverter

    override fun ModEntity.setVersions(versions: List<ModVersionEntity>) = copy(versions = versions)

}