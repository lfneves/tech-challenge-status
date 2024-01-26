package com.mvp.status.infrastruture.repository

import com.mvp.status.infrastruture.entity.StatusEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional


@Repository
interface StatusRepository : MongoRepository<StatusEntity, String> {

    fun findByExternalId(externalId: String): Optional<StatusEntity>
}