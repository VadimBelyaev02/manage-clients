package com.andersen.manageclients.repository

import com.andersen.manageclients.model.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


interface ClientRepository : JpaRepository<Client, UUID> {
    fun existsByEmail(email: String): Boolean
}