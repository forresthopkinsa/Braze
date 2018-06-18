package com.forresthopkinsa.braze.model

data class Modpack(override val slug: String,
                   override var name: String,
                   var author: String = "",
                   var description: String = "",
                   var link: String? = null,
                   var donate: String? = null,
                   var versionName: String,
                   var versionNumber: Int,
                   var forgeVersion: ForgeVersion,
                   var javaVersion: JavaVersion?,
                   var recommended: Boolean = false,
                   var memory: Int?,
                   var modList: List<Mod>) : DTO