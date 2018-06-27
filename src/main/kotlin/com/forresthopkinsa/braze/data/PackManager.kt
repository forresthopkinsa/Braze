package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.PackConverter
import com.forresthopkinsa.braze.model.DAO.PackConverter.PackEntity
import com.forresthopkinsa.braze.model.DAO.PackVersionConverter
import com.forresthopkinsa.braze.model.DAO.PackVersionConverter.PackVersionEntity
import com.forresthopkinsa.braze.model.IndexedPackVersion
import com.forresthopkinsa.braze.model.Pack
import com.forresthopkinsa.braze.model.PackVersion
import com.forresthopkinsa.braze.model.SimplePack
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
object PackManager : Manager<SimplePack, Pack, PackEntity, PackVersionEntity, PackVersion, IndexedPackVersion> {

    override lateinit var db: PackRepository
        @Autowired
        set

    override val converter = PackConverter

    override val versionConverter = PackVersionConverter

    override fun PackEntity.setVersions(versions: List<PackVersionEntity>) = copy(versions = versions)

}