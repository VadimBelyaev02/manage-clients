package com.andersen.manageclients.service.impl

import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.mapper.JobMapper
import com.andersen.manageclients.model.JobRequestDto
import com.andersen.manageclients.model.JobResponseDto
import com.andersen.manageclients.repository.JobRepository
import com.andersen.manageclients.service.JobService
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class JobServiceImpl(
    private val jobRepository: JobRepository,
    private val jobMapper: JobMapper
) : JobService {

    override fun getById(id: UUID): JobResponseDto {
        val job = jobRepository.findById(id).orElseThrow {
            EntityNotFoundException("Job with id = $id not found")
        }
        return jobMapper.toResponseDto(job)
    }

    override fun getAll(): List<JobResponseDto> {
        val jobs = jobRepository.findAll()
        return jobs.map { jobMapper.toResponseDto(it) }
    }

    override fun save(jobRequestDto: JobRequestDto): JobResponseDto {
        checkUniqueness(jobRequestDto)

        val job = jobMapper.toEntity(jobRequestDto)
        val savedJob = jobRepository.save(job)
        return jobMapper.toResponseDto(savedJob)
    }

    override fun update(id: UUID, jobRequestDto: JobRequestDto): JobResponseDto {
        checkUniqueness(jobRequestDto)

        val job = jobRepository.findById(id).orElseThrow{
            EntityNotFoundException("Job with id = $id not found")
        }
        jobMapper.updateEntityFromRequestDto(jobRequestDto, job)
        return jobMapper.toResponseDto(job)
    }

    override fun deleteById(id: UUID) {
        if (!jobRepository.existsById(id)) {
            throw EntityNotFoundException("Job with id = $id not found")
        }
        jobRepository.deleteById(id)
    }

    private fun checkUniqueness(jobRequestDto: JobRequestDto) {
        if (jobRepository.existsByName(jobRequestDto.name)) {
            throw EntityDuplicationException("Job with name = ${jobRequestDto.name} already exists")
        }
    }
}