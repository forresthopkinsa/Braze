package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.DataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import javax.transaction.Transactional

@NoRepositoryBean
@Transactional
interface DataRepository<T : DataEntity> : JpaRepository<T, String> {

    fun findBySlug(slug: String): T?

    fun deleteBySlug(slug: String): Int

}