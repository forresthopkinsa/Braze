package com.forresthopkinsa.braze.model

data class Mod(val slug: String,
               var name: String,
               var author: String = "",
               var description: String = "",
               var link: String? = null,
               var donate: String? = null,
               var versions: List<IndexedModVersion>) : Element {
    fun simplify() = SimpleMod(slug, name, author, description, link, donate)
}

data class IndexedModVersion(var name: String,
                             var index: Int,
                             var minForge: ForgeVersion,
                             var maxForge: ForgeVersion,
                             var md5: String? = null,
                             var size: Int? = null,
                             var dependencies: List<SimpleModVersion>) : Element {
    fun simplify() = ModVersion(name, minForge, maxForge, md5, size, dependencies)
}

data class SimpleMod(val slug: String,
                     val name: String,
                     val author: String,
                     val description: String,
                     val link: String?,
                     val donate: String?) : Element {
    fun expand() = Mod(slug, name, author, description, link, donate, emptyList())
}

data class SimpleModVersion(val slug: String,
                            val version: String) : Element

data class ModVersion(var name: String,
                      var minForge: ForgeVersion,
                      var maxForge: ForgeVersion,
                      var md5: String? = null,
                      var size: Int? = null,
                      var dependencies: List<SimpleModVersion>) : Element

internal fun List<ModVersion>.expand() = mapIndexed { k, v ->
    IndexedModVersion(v.name, k, v.minForge, v.maxForge, v.md5, v.size, v.dependencies)
}

internal fun List<IndexedModVersion>.simplify() = sortedBy { it.index }
        .map { ModVersion(it.name, it.minForge, it.maxForge, it.md5, it.size, it.dependencies) }