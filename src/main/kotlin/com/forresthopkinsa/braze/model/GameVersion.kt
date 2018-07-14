package com.forresthopkinsa.braze.model

enum class GameVersion(val number: String) {
    M172("1.7.2"),
    M174("1.7.4"),
    M175("1.7.5"),
    M176("1.7.6"),
    M177("1.7.7"),
    M178("1.7.8"),
    M179("1.7.9"),
    M1710("1.7.10");

    data class GameConstant(val name: String, val number: String)

    fun toConstant() = GameConstant(name, number)

    companion object {
        fun constants() = values().map(GameVersion::toConstant)
    }
}