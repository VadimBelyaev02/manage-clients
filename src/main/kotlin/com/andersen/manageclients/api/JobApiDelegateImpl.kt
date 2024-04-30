package com.andersen.manageclients.api

import com.andersen.manageclients.model.JobRequestDto
import com.andersen.manageclients.model.JobResponseDto
import com.andersen.manageclients.service.JobService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class JobApiDelegateImpl(
    private val jobService: JobService
):JobsApiDelegate {

    override fun deleteJob(jobId: String): ResponseEntity<Unit> {
        jobService.deleteById(UUID.fromString(jobId))
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    override fun getAllJobs(): ResponseEntity<List<JobResponseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAll())
    }

    override fun getJob(jobId: String): ResponseEntity<JobResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getById(UUID.fromString(jobId)))
    }

    override fun saveJob(jobRequestDto: JobRequestDto): ResponseEntity<JobResponseDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.save(jobRequestDto))
    }

    override fun updateJob(jobId: String, jobRequestDto: JobRequestDto): ResponseEntity<JobResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.update(UUID.fromString(jobId), jobRequestDto))
    }
}