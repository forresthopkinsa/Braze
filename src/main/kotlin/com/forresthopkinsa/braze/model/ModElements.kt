package com.forresthopkinsa.braze.model

data class Mod(val slug: String,
               var name: String,
               var author: String = "",
               var description: String = "",
               var link: String? = null,
               var donate: String? = null,
               var versions: List<ModVersion>) : Element {
    fun simplify() = SimpleMod(slug, name, author, description, link, donate)
}

data class ModVersion(var versionName: String,
                      var versionNumber: Int,
                      var minForge: ForgeVersion,
                      var maxForge: ForgeVersion,
                      var md5: String? = null,
                      var size: Int? = null,
                      var dependencies: List<SimpleModVersion>) : Element {
    fun simplify() = UnindexedModVersion(versionName, minForge, maxForge, md5, size, dependencies)
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
                            val versionName: String) : Element

data class UnindexedModVersion(var versionName: String,
                               var minForge: ForgeVersion,
                               var maxForge: ForgeVersion,
                               var md5: String? = null,
                               var size: Int? = null,
                               var dependencies: List<SimpleModVersion>) : Element

internal fun List<UnindexedModVersion>.expand() = mapIndexed { k, v ->
    ModVersion(v.versionName, k, v.minForge, v.maxForge, v.md5, v.size, v.dependencies)
}

internal fun List<ModVersion>.simplify() = sortedBy { it.versionNumber }
        .map { UnindexedModVersion(it.versionName, it.minForge, it.maxForge, it.md5, it.size, it.dependencies) }