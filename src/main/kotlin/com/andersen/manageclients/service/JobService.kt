package com.andersen.manageclients.service

import com.andersen.manageclients.model.JobRequestDto
import com.andersen.manageclients.model.JobResponseDto
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID

interface JobService {

    fun getById(id: UUID) : JobResponseDto

    fun getAll() : List<JobResponseDto>

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    fun save(jobRequestDto: JobRequestDto) : JobResponseDto

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    fun update(id: UUID, jobRequestDto: JobRequestDto) : JobResponseDto

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    fun deleteById(id: UUID)
}