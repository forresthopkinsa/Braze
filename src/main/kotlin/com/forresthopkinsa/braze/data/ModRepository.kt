package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModConverter.ModEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface ModRepository : JpaRepository<ModEntity, String> {

    fun findBySlug(slug: String): ModEntity // todo: nullable

    fun deleteBySlug(slug: String): Int

}