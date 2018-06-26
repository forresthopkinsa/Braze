package com.forresthopkinsa.braze.model

data class Pack(val slug: String,
                var name: String,
                var author: String = "",
                var description: String = "",
                var link: String? = null,
                var donate: String? = null,
                val versions: List<PackVersion>) : Element {
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

data class PackVersion(var version: String,
                       var build: Int,
                       var forgeVersion: ForgeVersion,
                       var javaVersion: JavaVersion?,
                       var recommended: Boolean = false,
                       var memory: Int?,
                       var modList: List<SimpleModVersion>) : Element