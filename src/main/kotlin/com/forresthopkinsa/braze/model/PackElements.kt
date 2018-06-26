package com.forresthopkinsa.braze.model

data class Pack(val slug: String,
                var name: String,
                var author: String = "",
                var description: String = "",
                var link: String? = null,
                var donate: String? = null,
                val versions: List<IndexedPackVersion>) : Element {
    fun simplify() = SimplePack(slug, name, author, description, link, donate)
}

data class SimplePack(val slug: String,
                      var name: String,
                      var author: String,
                      var description: String,
                      var link: String?,
                      var donate: String?) : Element {
    fun expand() = Pack(slug, name, author, description, link, donate, emptyList())
}

data class IndexedPackVersion(var name: String,
                              var index: Int,
                              var forgeVersion: ForgeVersion,
                              var javaVersion: JavaVersion?,
                              var recommended: Boolean = false,
                              var memory: Int?,
                              var modList: List<SimpleModVersion>) : Element {
    fun simplify() = PackVersion(name, forgeVersion, javaVersion, recommended, memory, modList)
}

data class PackVersion(var name: String,
                       var forgeVersion: ForgeVersion,
                       var javaVersion: JavaVersion?,
                       var recommended: Boolean = false,
                       var memory: Int?,
                       var modList: List<SimpleModVersion>) : Element

internal fun List<PackVersion>.expand() = mapIndexed { k, v ->
    IndexedPackVersion(v.name, k, v.forgeVersion, v.javaVersion, v.recommended, v.memory, v.modList)
}

internal fun List<IndexedPackVersion>.simplify() = sortedBy { it.index }
        .map { PackVersion(it.name, it.forgeVersion, it.javaVersion, it.recommended, it.memory, it.modList) }