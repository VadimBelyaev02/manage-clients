package com.andersen.manageclients.service.impl

import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.mapper.PositionMapper
import com.andersen.manageclients.model.Job
import com.andersen.manageclients.model.JobPosition
import com.andersen.manageclients.model.JobPositionId
import com.andersen.manageclients.model.PositionRequestDto
import com.andersen.manageclients.model.PositionResponseDto
import com.andersen.manageclients.repository.JobRepository
import com.andersen.manageclients.repository.PositionRepository
import com.andersen.manageclients.service.PositionService
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.collections.ArrayList

@Service
class PositionServiceImpl(
    private val positionRepository: PositionRepository,
    private val positionMapper: PositionMapper,
    private val jobRepository: JobRepository
) : PositionService {
    override fun getById(id: UUID): PositionResponseDto {
        val position = positionRepository.findById(id).orElseThrow {
            EntityNotFoundException("Position with id = $id not found")
        }
        return positionMapper.toResponseDto(position)
    }

    override fun getAll(): List<PositionResponseDto> {
        val positions = positionRepository.findAll()
        return positions.map { positionMapper.toResponseDto(it) }
    }

    @Transactional
    override fun save(positionRequestDto: PositionRequestDto): PositionResponseDto {
        checkUniqueness(positionRequestDto)

        val position = positionMapper.toEntity(positionRequestDto)

        val jobs = getJobs(positionRequestDto.jobIds)

        val savedPosition = positionRepository.save(position)

        savedPosition.positionJobs = jobs.map { job ->
            JobPosition(
                job = job,
                position = savedPosition
            )
        }

        return positionMapper.toResponseDto(savedPosition)
    }

    @Transactional
    override fun update(id: UUID, positionRequestDto: PositionRequestDto): PositionResponseDto {
        checkUniqueness(positionRequestDto)

        val position = positionRepository.findById(id).orElseThrow {
            EntityNotFoundException("Position with id = $id not found")
        }

        positionMapper.updateEntityFromRequestDto(positionRequestDto, position)

        val jobs = getJobs(positionRequestDto.jobIds)

        position.positionJobs = jobs.map { job ->
            JobPosition(
                job = job,
                position = position
            )
        }

        return positionMapper.toResponseDto(position)
    }

    @Transactional
    override fun deleteById(id: UUID) {
        if (!positionRepository.existsById(id)) {
            throw EntityNotFoundException("Position with id = $id not found")
        }
        positionRepository.deleteById(id)
    }

    private fun getJobs(jobIds: List<String>): List<Job> {
        val jobs = jobRepository.findAllByJobIds(jobIds.map { UUID.fromString(it) })
        if (jobs.size != jobIds.size) {
            throw EntityNotFoundException("One or more jobs not found by ids")
        }
        return jobs
    }

    private fun checkUniqueness(positionRequestDto: PositionRequestDto) {
        if (positionRepository.existsByName(positionRequestDto.name)) {
            throw EntityDuplicationException("Position with name = ${positionRequestDto.name} already exists")
        }
    }
}