package com.forresthopkinsa.braze.model

data class Mod(override val slug: String,
               override var name: String,
               var author: String = "",
               var description: String = "",
               var link: String? = null,
               var donate: String? = null,
               var versionName: String,
               var versionNumber: Int,
               var forgeVersions: ClosedRange<ForgeVersion>,
               var md5: String? = null,
               var size: Int? = null,
               var dependencies: List<Mod>) : DTO