package com.andersen.manageclients.mapper

import com.andersen.manageclients.model.Job
import com.andersen.manageclients.model.JobRequestDto
import com.andersen.manageclients.model.JobResponseDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.MappingTarget

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = [PositionMapper::class])
interface JobMapper {

    @Mapping(target = "id", ignore = true)
    fun toEntity(jobRequestDto: JobRequestDto): Job

    //    @Mapping(
//        target = "positions",
//        expression = "kotlin(jobPositions.map{positionsMapper.toResponseDto(it)})"
//    )
    @Mapping(target = "positions", source = "jobPositions") // Добавляем маппинг для списка jobPositions

    fun toResponseDto(job: Job): JobResponseDto

    @Mapping(target = "id", ignore = true)
    fun updateEntityFromRequestDto(jobRequestDto: JobRequestDto, @MappingTarget job: Job)
}