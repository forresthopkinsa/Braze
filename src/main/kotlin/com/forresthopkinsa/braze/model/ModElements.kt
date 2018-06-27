package com.forresthopkinsa.braze.model

data class Mod(override val slug: String,
               override var name: String,
               var author: String = "",
               var description: String = "",
               var link: String? = null,
               var donate: String? = null,
               var versions: List<IndexedModVersion>) : Element<Mod, SimpleMod> {
    override fun simplify() = SimpleMod(slug, name, author, description, link, donate)
}

data class IndexedModVersion(override var name: String,
                             override var index: Int,
                             var minForge: ForgeVersion,
                             var maxForge: ForgeVersion,
                             var md5: String? = null,
                             var size: Int? = null,
                             var dependencies: List<SimpleModVersion>) : IndexedVersion<ModVersion> {
    override fun simplify() = ModVersion(name, minForge, maxForge, md5, size, dependencies)
}

data class SimpleMod(override val slug: String,
                     override val name: String,
                     val author: String,
                     val description: String,
                     val link: String?,
                     val donate: String?) : SimpleElement<SimpleMod, Mod> {
    override fun expand() = Mod(slug, name, author, description, link, donate, emptyList())
}

data class SimpleModVersion(val slug: String,
                            val version: String) : Data

data class ModVersion(override var name: String,
                      var minForge: ForgeVersion,
                      var maxForge: ForgeVersion,
                      var md5: String? = null,
                      var size: Int? = null,
                      var dependencies: List<SimpleModVersion>) : Version

internal fun List<ModVersion>.expand() = mapIndexed { k, v ->
    IndexedModVersion(v.name, k, v.minForge, v.maxForge, v.md5, v.size, v.dependencies)
}

internal fun List<IndexedModVersion>.simplify() = sortedBy { it.index }
        .map { ModVersion(it.name, it.minForge, it.maxForge, it.md5, it.size, it.dependencies) }