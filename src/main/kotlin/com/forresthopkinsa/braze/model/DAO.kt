package com.forresthopkinsa.braze.model

import com.forresthopkinsa.braze.model.DAO.ModConverter.ModEntity
import com.forresthopkinsa.braze.model.DAO.ModVersionConverter.ModVersionEntity
import com.forresthopkinsa.braze.model.DAO.PackConverter.PackEntity
import com.forresthopkinsa.braze.model.DAO.PackVersionConverter.PackVersionEntity
import com.forresthopkinsa.braze.model.DAO.SimpleModVersionConverter.SimpleModVersionEntity
import com.forresthopkinsa.braze.spring.name
import com.forresthopkinsa.braze.spring.version
import javax.persistence.*

class DAO {

    interface DataEntity {
        fun component1(): Any? // ensures that this is a data class; componentN() functions are autogenerated
    }

    interface EntityConverter<T : DataEntity, U : Element> {
        fun fromElement(element: U): T
        fun fromEntity(entity: T): U
    }

    object ModConverter : EntityConverter<ModEntity, Mod> {

        @Entity
        @Table(name = "MODS")
        data class ModEntity(
                @Id
                val slug: String,

                @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
                @JoinColumn(name = "PARENT")
                @OrderColumn(name = "INDEX")
                val versions: List<ModVersionEntity>,

                var name: String,
                var author: String,
                var description: String,
                var link: String?,
                var donate: String?
        ) : DataEntity

        override fun fromElement(element: Mod): ModEntity = element.run {
            ModEntity(
                    slug = slug,
                    versions = versions.simplify().map(ModVersionConverter::fromElement),
                    name = name,
                    author = author,
                    description = description,
                    link = link,
                    donate = donate
            )
        }

        override fun fromEntity(entity: ModEntity): Mod = entity.run {
            Mod(
                    slug = slug,
                    name = name,
                    author = author,
                    description = description,
                    link = link,
                    donate = donate,
                    versions = versions.map(ModVersionConverter::fromEntity).expand()
            )
        }

    }

    object ModVersionConverter : EntityConverter<ModVersionEntity, ModVersion> {

        @Entity
        @Table(name = "MODVERSIONS")
        data class ModVersionEntity(
                @Id @GeneratedValue
                val id: Int = 0,

                @ElementCollection
                @CollectionTable(name = "DEPENDENCIES", joinColumns = [JoinColumn(name = "PARENT")])
                val dependencies: List<SimpleModVersionEntity>,

                @Enumerated(EnumType.STRING)
                var minForge: ForgeVersion,

                @Enumerated(EnumType.STRING)
                var maxForge: ForgeVersion,

                var name: String,
                var md5: String?,
                var size: Int?
        ) : DataEntity

        override fun fromEntity(entity: ModVersionEntity) = entity.run {
            ModVersion(
                    name = name,
                    minForge = minForge,
                    maxForge = maxForge,
                    md5 = md5,
                    size = size,
                    dependencies = dependencies.map(SimpleModVersionConverter::fromEntity)
            )
        }

        override fun fromElement(element: ModVersion) = element.run {
            ModVersionEntity(
                    dependencies = dependencies.map(SimpleModVersionConverter::fromElement),
                    minForge = minForge,
                    maxForge = maxForge,
                    name = name,
                    md5 = md5,
                    size = size
            )
        }
    }

    object SimpleModVersionConverter : EntityConverter<SimpleModVersionEntity, SimpleModVersion> {

        @Embeddable
        data class SimpleModVersionEntity(val slug: String,
                                          val version: String) : DataEntity

        override fun fromElement(element: SimpleModVersion) = element.run {
            SimpleModVersionEntity(slug, version)
        }

        override fun fromEntity(entity: SimpleModVersionEntity) = entity.run {
            SimpleModVersion(slug, version)
        }

    }

    object PackConverter : EntityConverter<PackEntity, Pack> {

        @Entity
        @Table(name = "PACKS")
        data class PackEntity(
                @Id
                val slug: String,

                @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
                @JoinColumn(name = "PARENT")
                @OrderColumn(name = "INDEX")
                val versions: List<PackVersionEntity>,

                var name: String,
                var author: String,
                var description: String,
                var link: String?,
                var donate: String?) : DataEntity

        override fun fromElement(element: Pack): PackEntity {
            TODO("not implemented")
        }

        override fun fromEntity(entity: PackEntity): Pack {
            TODO("not implemented")
        }

    }

    object PackVersionConverter : EntityConverter<PackVersionEntity, PackVersion> {

        @Entity
        @Table(name = "PACKVERSIONS")
        data class PackVersionEntity(
                @Id @GeneratedValue
                val id: Int = 0,

                @ElementCollection
                @CollectionTable(name = "INCLUDEDMODS", joinColumns = [JoinColumn(name = "PARENT")])
                var modList: List<SimpleModVersionEntity>,

                @Enumerated(EnumType.STRING)
                var forgeVersion: ForgeVersion,

                @Enumerated(EnumType.STRING)
                var javaVersion: JavaVersion?,

                var name: String,
                var build: Int,
                var recommended: Boolean,
                var memory: Int?
        ) : DataEntity

        override fun fromElement(element: PackVersion) = element.run {
            PackVersionEntity(
                    modList = modList.map(SimpleModVersionConverter::fromElement),
                    forgeVersion = forgeVersion,
                    javaVersion = javaVersion,
                    name = name,
                    build = build,
                    recommended = recommended,
                    memory = memory
            )
        }

        override fun fromEntity(entity: PackVersionEntity) = entity.run {
            PackVersion(
                    version = version,
                    build = build,
                    forgeVersion = forgeVersion,
                    javaVersion = javaVersion,
                    recommended = recommended,
                    memory = memory,
                    modList = modList.map(SimpleModVersionConverter::fromEntity)
            )
        }

    }

}