package com.andersen.manageclients.repository

import com.andersen.manageclients.model.Position
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PositionRepository: JpaRepository<Position, UUID> {

    fun existsByName(name: String) : Boolean
}