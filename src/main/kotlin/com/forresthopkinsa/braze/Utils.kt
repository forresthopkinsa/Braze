package com.forresthopkinsa.braze

import com.forresthopkinsa.braze.model.DAO
import com.forresthopkinsa.braze.model.Element
import com.google.gson.Gson

private val gson = Gson()

internal fun <T : DAO.DataEntity, U : Element> U?.toEntity(converter: DAO.EntityConverter<T, U>): T? =
        this?.let { converter.fromElement(it) }

internal fun <T : DAO.DataEntity, U : Element> T?.toElement(converter: DAO.EntityConverter<T, U>): U? =
        this?.let { converter.fromEntity(it) }

internal fun Any.toJson(): String = gson.toJson(this)

internal inline fun <reified T> fromJson(json: String) = gson.fromJson(json, T::class.java)