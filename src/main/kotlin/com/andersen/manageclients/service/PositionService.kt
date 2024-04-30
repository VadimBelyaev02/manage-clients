package com.andersen.manageclients.service

import com.andersen.manageclients.model.PositionRequestDto
import com.andersen.manageclients.model.PositionResponseDto
import java.util.UUID

interface PositionService {

    fun getById(id: UUID) : PositionResponseDto

    fun getAll() : List<PositionResponseDto>

    fun save(positionRequestDto: PositionRequestDto) : PositionResponseDto

    fun update(id: UUID, positionRequestDto: PositionRequestDto) : PositionResponseDto

    fun deleteById(id: UUID)
}