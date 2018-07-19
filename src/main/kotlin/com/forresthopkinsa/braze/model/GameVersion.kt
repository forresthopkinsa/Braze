package com.forresthopkinsa.braze.model

enum class GameVersion(val number: String) {
    M172("1.7.2"),
    M1710("1.7.10"),
    M180("1.8"),
    M188("1.8.8"),
    M189("1.8.9");

    data class GameConstant(val name: String, val number: String)

    fun toConstant() = GameConstant(name, number)

    companion object {
        fun constants() = values().map(GameVersion::toConstant)
    }
}