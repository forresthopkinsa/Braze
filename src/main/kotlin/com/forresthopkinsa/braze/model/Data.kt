package com.forresthopkinsa.braze.model

interface Data {
    fun component1(): Any?
}

interface Slugged : Data {
    val slug: String
    val name: String
}

interface Element<T : Element<T, U>, U : SimpleElement<U, T>> : Slugged {
    fun simplify(): U
}

interface SimpleElement<T : SimpleElement<T, U>, U : Element<U, T>> : Slugged {
    fun expand(): U
}

interface Version : Data {
    val name: String
}

interface IndexedVersion<T : Version> : Version {
    val index: Int
    fun simplify(): T
}