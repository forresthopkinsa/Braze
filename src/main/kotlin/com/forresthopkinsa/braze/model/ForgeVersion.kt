package com.forresthopkinsa.braze.model

import com.forresthopkinsa.braze.model.GameVersion.*

enum class ForgeVersion(val build: Int, val gameVersion: GameVersion) {
    F1121(1121, M172),
    F1161(1161, M172),

    F1388(1388, M1710),
    F1492(1492, M1710),
    F1558(1558, M1710),

    F1237(1237, M180),
    F1563(1563, M180),
    F1577(1577, M180),

    F1575(1575, M188),
    F1655(1655, M188),

    F1656(1656, M189),
    F1722(1722, M189),
    F2318(2318, M189);

    data class ForgeConstant(val name: String, val build: Int, val gameVersion: GameConstant)

    fun toConstant() = ForgeConstant(name, build, gameVersion.toConstant())

    companion object {
        fun constants() = values().map(ForgeVersion::toConstant)
    }
}