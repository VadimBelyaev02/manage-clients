package com.andersen.manageclients.api

import com.andersen.manageclients.model.PositionRequestDto
import com.andersen.manageclients.model.PositionResponseDto
import com.andersen.manageclients.service.PositionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class PositionDelegateImpl(
    private val positionService: PositionService,
) : PositionsApiDelegate {
    override fun deletePosition(positionId: String): ResponseEntity<Unit> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    override fun getAllPositions(): ResponseEntity<List<PositionResponseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(positionService.getAll())
    }

    override fun getPosition(positionId: String): ResponseEntity<PositionResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(positionService.getById(UUID.fromString(positionId)))
    }

    override fun savePosition(positionRequestDto: PositionRequestDto): ResponseEntity<PositionResponseDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(positionService.save(positionRequestDto))
    }

    override fun updatePosition(
        positionId: String,
        request: PositionRequestDto
    ): ResponseEntity<PositionResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(positionService.update(UUID.fromString(positionId), request))
    }
}
