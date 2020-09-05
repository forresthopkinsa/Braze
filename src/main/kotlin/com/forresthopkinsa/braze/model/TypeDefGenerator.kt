package com.forresthopkinsa.braze.model

import com.forresthopkinsa.braze.spring.RestController
import me.ntrrgc.tsGenerator.TypeScriptGenerator

fun main() {
    val classes = setOf(
        RestController.Constants::class,
        ForgeVersion::class,
        GameVersion::class,
        JavaVersion::class,
        Mod::class,
        SimpleMod::class,
        ModVersion::class,
        IndexedModVersion::class,
        Pack::class,
        SimplePack::class,
        PackVersion::class,
        IndexedPackVersion::class
    )
    val ignored = setOf(
        Element::class,
        SimpleElement::class,
        Version::class,
        IndexedVersion::class,
        Data::class
    )
    val definitions = TypeScriptGenerator(rootClasses = classes, ignoreSuperclasses = ignored).definitionsText
    println(definitions)
}