package com.andersen.manageclients.service

import com.andersen.manageclients.model.PositionRequestDto
import com.andersen.manageclients.model.PositionResponseDto
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID

interface PositionService {

    fun getById(id: UUID) : PositionResponseDto

    fun getAll() : List<PositionResponseDto>

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    fun save(positionRequestDto: PositionRequestDto) : PositionResponseDto

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    fun update(id: UUID, positionRequestDto: PositionRequestDto) : PositionResponseDto

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    fun deleteById(id: UUID)
}