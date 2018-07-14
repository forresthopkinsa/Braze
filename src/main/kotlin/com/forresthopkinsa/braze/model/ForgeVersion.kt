package com.forresthopkinsa.braze.model

import com.forresthopkinsa.braze.model.GameVersion.*

enum class ForgeVersion(val build: Int, val gameVersion: GameVersion) {
    F1121(1121, M172),
    F1161(1161, M172),

    F1388(1388, M1710),
    F1492(1492, M1710),
    F1558(1558, M1710);

    data class ForgeConstant(val name: String, val build: Int, val gameVersion: GameConstant)

    fun toConstant() = ForgeConstant(name, build, gameVersion.toConstant())

    companion object {
        fun constants() = values().map(ForgeVersion::toConstant)
    }
}