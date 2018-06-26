package com.forresthopkinsa.braze.data

import com.forresthopkinsa.braze.model.DAO.PackConverter.PackEntity
import org.springframework.stereotype.Repository

@Repository
interface PackRepository : DataRepository<PackEntity>