package com.forresthopkinsa.braze.model

enum class JavaVersion(val version: String) {
    J7("7"),
    J8("8"),
    J9("9"),
    J10("10"),
    J11("11");

    data class JavaConstant(val name: String, val version: String)

    fun toConstant() = JavaConstant(name, version)

    companion object {
        fun constants() = values().map(JavaVersion::toConstant)
    }
}