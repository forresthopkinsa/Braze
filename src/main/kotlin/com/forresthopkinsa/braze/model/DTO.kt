package com.forresthopkinsa.braze.model

import com.google.gson.annotations.SerializedName

/** solder/api */
data class API(
        @SerializedName("api") val api: String,
        @SerializedName("version") val version: String,
        @SerializedName("stream") val stream: String
)

/** solder/api/mod (undocumented) */
data class ModList(
        @SerializedName("mods") val mods: Map<String, String>
)

/** solder/api/mod/:slug */
data class Mod(
        @SerializedName("name") val slug: String,
        @SerializedName("pretty_name") val name: String,
        @SerializedName("author") val author: String,
        @SerializedName("description") val description: String,
        @SerializedName("link") val link: String,
        @SerializedName("donate") val donate: String = "",
        @SerializedName("versions") val versions: List<String>
)

/** solder/api/mod/:slug/:version */
data class VersionedMod(
        @SerializedName("md5") val md5: String,
        /** .zip size in bytes (undocumented) */
        @SerializedName("filesize") val size: String,
        @SerializedName("url") val url: String
)

/** solder/api/modpack (undocumented) */
data class ModpackList(
        @SerializedName("modpacks") val modpacks: Map<String, String>,
        @SerializedName("mirror_url") val modsUrl: String
)

/** solder/api/modpack/:slug */
data class Modpack(
        @SerializedName("name") val slug: String,
        @SerializedName("display_name") val name: String,
        @SerializedName("url") val url: String,
        @SerializedName("icon") val icon: String,
        @SerializedName("icon_md5") val iconHash: String,
        @SerializedName("logo") val logo: String,
        @SerializedName("logo_md5") val logoHash: String,
        @SerializedName("background") val background: String,
        @SerializedName("background_md5") val backgroundHash: String,
        @SerializedName("recommended") val recommended: String,
        @SerializedName("latest") val latest: String,
        @SerializedName("builds") val builds: List<String>
)

/** solder/api/modpack/:slug/:build */
data class VersionedModpack(
        /** Minecraft version to use, e.g. "1.7.10" */
        @SerializedName("minecraft") val minecraft: String,
        /** Java version to use, e.g. "1.8" (undocumented) */
        @SerializedName("java") val java: String,
        /** Required memory in MB (undocumented) */
        @SerializedName("memory") val memory: String,
        /** Probably supposed to be the forge build version, but unused */
        @SerializedName("forge") val forge: String? = null,
        @SerializedName("mods") val mods: List<ListedMod>
)

data class ListedMod(
        @SerializedName("name") val name: String,
        @SerializedName("version") val version: String,
        @SerializedName("md5") val md5: String,
        @SerializedName("filesize") val filesize: String,
        @SerializedName("url") val url: String
)