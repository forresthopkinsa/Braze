package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModConverter.ModVersionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface ModRepository : JpaRepository<ModVersionEntity, Int> {

    fun findByParentSlug(slug: String): List<ModVersionEntity>

    fun findByParentSlugAndVersionName(slug: String, versionName: String): ModVersionEntity

}