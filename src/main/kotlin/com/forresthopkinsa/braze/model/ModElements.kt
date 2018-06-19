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
                      var dependencies: List<Mod>) : Element

data class SimpleMod(val slug: String,
                     val name: String,
                     val author: String,
                     val description: String,
                     val link: String?,
                     val donate: String?) : Element {
    fun expand() = Mod(slug, name, author, description, link, donate, emptyList())
}