package com.andersen.manageclients.mapper

import com.andersen.manageclients.model.Job
import com.andersen.manageclients.model.JobPosition
import com.andersen.manageclients.model.JobPositionId
import com.andersen.manageclients.model.JobRequestDto
import com.andersen.manageclients.model.JobResponseDto
import com.andersen.manageclients.model.PositionResponseDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.MappingTarget

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = [PositionMapper::class])
interface JobMapper {

    @Mapping(target = "id", ignore = true)
    fun toEntity(jobRequestDto: JobRequestDto): Job


    @Mapping(target = "id", source = "id")
    fun toResponseDto(job: Job): JobResponseDto

    @Mapping(target = "id", ignore = true)
    fun updateEntityFromRequestDto(jobRequestDto: JobRequestDto, @MappingTarget job: Job)


//    fun mapPositionsToResponseDto(jobPositions: List<JobPosition>): List<PositionResponseDto>

}