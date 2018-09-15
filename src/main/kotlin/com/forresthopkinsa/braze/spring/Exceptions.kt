package com.forresthopkinsa.braze.spring

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

sealed class RestException(reason: String) : RuntimeException(reason)


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested element not found")
sealed class NotFoundException(reason: String) : RestException(reason)

class ModNotFoundException(slug: String) : NotFoundException("Mod '$slug' does not exist")

class ModVersionNotFoundException(slug: String, version: String) :
    NotFoundException("ModVersion '$slug' : '$version' does not exist")


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Slug is not correctly formed")
class InvalidSlugException(slug: String) : RestException("Incorrectly formed slug: $slug")

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Version is not correctly formed")
class InvalidVersionException(version: String) : RestException("Incorrectly formed version: $version")