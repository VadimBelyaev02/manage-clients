package com.andersen.manageclients.repository


import com.andersen.manageclients.model.Job
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface JobRepository: JpaRepository<Job, UUID> {

    fun existsByName(name: String) : Boolean

    @Query("""
        SELECT j FROM Job j
        WHERE j.id in :jobIds
    """)
    fun findAllByJobIds(jobIds: List<UUID>): List<Job>
}