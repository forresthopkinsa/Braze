package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.ModVersionConverter.ModVersionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface ModVersionRepository : JpaRepository<ModVersionEntity, Int>