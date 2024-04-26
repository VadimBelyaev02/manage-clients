package com.andersen.manageclients.mapper

import com.andersen.manageclients.model.Client
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

//@Mapper
interface ClientMapper {


    fun toResponseDto(client: Client): ClientResponseDto

    fun toEntity(clientRequestDto: ClientRequestDto): Client

    @Mapping(target = "id", ignore = true)
    fun updateEntityFromRequestDto(clientRequestDto: ClientRequestDto, @MappingTarget client: Client)

}