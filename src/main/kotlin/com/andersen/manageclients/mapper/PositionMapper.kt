package com.andersen.manageclients.mapper

import com.andersen.manageclients.model.JobPosition
import com.andersen.manageclients.model.JobPositionId
import com.andersen.manageclients.model.JobResponseDto
import com.andersen.manageclients.model.Position
import com.andersen.manageclients.model.PositionRequestDto
import com.andersen.manageclients.model.PositionResponseDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = [JobMapper::class])
interface PositionMapper {

    fun toEntity(positionRequestDto: PositionRequestDto) : Position

    @Mapping(target = "jobsIds", expression = "java(position.getPositionJobs().stream()" +
            ".map(positionJob -> positionJob.getJob().getId().toString()).toList())")
    fun toResponseDto(position: Position) : PositionResponseDto

    fun updateEntityFromRequestDto(positionRequestDto: PositionRequestDto, @MappingTarget position: Position)
}