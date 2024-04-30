package com.andersen.manageclients.service

import com.andersen.manageclients.model.JobRequestDto
import com.andersen.manageclients.model.JobResponseDto
import java.util.UUID

interface JobService {

    fun getById(id: UUID) : JobResponseDto

    fun getAll() : List<JobResponseDto>

    fun save(jobRequestDto: JobRequestDto) : JobResponseDto

    fun update(id: UUID, jobRequestDto: JobRequestDto) : JobResponseDto

    fun deleteById(id: UUID)
}