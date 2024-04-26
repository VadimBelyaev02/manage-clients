package com.andersen.manageclients.mapper

import com.andersen.manageclients.model.Client
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.openapitools.client.model.ClientRequestDto
import org.openapitools.client.model.ClientResponseDto

//@Mapper
interface ClientMapper {


    fun toResponseDto(client: Client): ClientResponseDto

    fun toEntity(clientRequestDto: ClientRequestDto): Client

    @Mapping(target = "id", ignore = true)
    fun updateEntityFromRequestDto(clientRequestDto: ClientRequestDto, @MappingTarget client: Client)

}