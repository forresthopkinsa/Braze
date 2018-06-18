package com.forresthopkinsa.braze.model

import com.forresthopkinsa.braze.model.GameVersion.M1710
import com.forresthopkinsa.braze.model.GameVersion.M172

enum class ForgeVersion(val build: Int, val gameVersion: GameVersion) {
    F1121(1121, M172),
    F1161(1161, M172),

    F1388(1388, M1710),
    F1492(1492, M1710),
    F1558(1558, M1710)
}