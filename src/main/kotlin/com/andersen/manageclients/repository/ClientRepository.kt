package com.andersen.manageclients.repository

import com.andersen.manageclients.model.Client
import com.andersen.manageclients.model.SearchCriteria
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID


interface ClientRepository : JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): Optional<Client>
}