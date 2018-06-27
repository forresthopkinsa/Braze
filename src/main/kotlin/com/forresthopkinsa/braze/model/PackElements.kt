package com.forresthopkinsa.braze.model

data class Pack(override val slug: String,
                override var name: String,
                var author: String = "",
                var description: String = "",
                var link: String? = null,
                var donate: String? = null,
                override val versions: List<IndexedPackVersion>) : Element<Pack, SimplePack> {
    override fun simplify() = SimplePack(slug, name, author, description, link, donate)
}

data class SimplePack(override val slug: String,
                      override var name: String,
                      var author: String,
                      var description: String,
                      var link: String?,
                      var donate: String?) : SimpleElement<SimplePack, Pack> {
    override fun expand() = Pack(slug, name, author, description, link, donate, emptyList())
}

data class IndexedPackVersion(override var name: String,
                              override var index: Int,
                              var forgeVersion: ForgeVersion,
                              var javaVersion: JavaVersion?,
                              var recommended: Boolean = false,
                              var memory: Int?,
                              var modList: List<SimpleModVersion>) : IndexedVersion<PackVersion> {
    override fun simplify() = PackVersion(name, forgeVersion, javaVersion, recommended, memory, modList)
}

data class PackVersion(override var name: String,
                       var forgeVersion: ForgeVersion,
                       var javaVersion: JavaVersion?,
                       var recommended: Boolean = false,
                       var memory: Int?,
                       var modList: List<SimpleModVersion>) : Version

internal fun List<PackVersion>.expand() = mapIndexed { k, v ->
    IndexedPackVersion(v.name, k, v.forgeVersion, v.javaVersion, v.recommended, v.memory, v.modList)
}

internal fun List<IndexedPackVersion>.simplify() = sortedBy { it.index }
        .map { PackVersion(it.name, it.forgeVersion, it.javaVersion, it.recommended, it.memory, it.modList) }