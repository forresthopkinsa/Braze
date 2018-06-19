package com.forresthopkinsa.braze.model

data class Mod(val slug: String,
               var name: String,
               var author: String = "",
               var description: String = "",
               var link: String? = null,
               var donate: String? = null,
               var versions: List<ModVersion>?) : Element

data class ModVersion(var versionName: String,
                      var versionNumber: Int,
                      var minForge: ForgeVersion,
                      var maxForge: ForgeVersion,
                      var md5: String? = null,
                      var size: Int? = null,
                      var dependencies: List<Mod>) : Element