package com.forresthopkinsa.braze.model

import com.forresthopkinsa.braze.model.DAO.ModConverter.ModVersionEntity
import javax.persistence.*

sealed class DAO {

    interface DAO {
        fun component1(): Any? // ensures that this is a data class; componentN() functions are autogenerated
    }

    interface EntityConverter<T : DAO, U : DTO> {
        fun fromDto(dto: U): T
        fun fromDao(dao: T): U
    }

    object ModConverter : EntityConverter<ModVersionEntity, Mod> {

        @Entity
        @Table(name = "Mods")
        data class ModEntity(
                @Id
                val slug: String,

                var name: String,
                var author: String,
                var description: String,
                var link: String?,
                var donate: String?
        ) : DAO

        @Entity
        @Table(name = "ModVersions")
        data class ModVersionEntity(
                @Id @GeneratedValue
                val id: Int = 0,

                @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
                @JoinColumn
                val parent: ModEntity,

                @Enumerated(EnumType.STRING)
                var minForge: ForgeVersion,

                @Enumerated(EnumType.STRING)
                var maxForge: ForgeVersion,

                var versionName: String,
                var versionNumber: Int,
                var md5: String?,
                var size: Int?
        ) : DAO

        override fun fromDao(dao: ModVersionEntity) = dao.run {
            Mod(
                    slug = parent.slug,
                    name = parent.name,
                    author = parent.author,
                    description = parent.description,
                    link = parent.link,
                    donate = parent.donate,
                    versionName = versionName,
                    versionNumber = versionNumber,
                    forgeVersions = minForge..maxForge,
                    md5 = md5,
                    size = size,
                    dependencies = listOf() // todo
            )
        }

        override fun fromDto(dto: Mod) = dto.run {
            ModVersionEntity(
                    parent = ModEntity(slug, name, author, description, link, donate),
                    minForge = forgeVersions.start,
                    maxForge = forgeVersions.endInclusive,
                    versionName = versionName,
                    versionNumber = versionNumber,
                    md5 = md5,
                    size = size
            )
        }

    }

}