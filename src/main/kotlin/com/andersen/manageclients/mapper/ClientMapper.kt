package com.andersen.manageclients.mapper

import com.andersen.manageclients.model.Client
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.RegistrationRequestDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.MappingTarget

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface ClientMapper {


    fun toResponseDto(client: Client): ClientResponseDto

    fun toEntity(clientRequestDto: ClientRequestDto): Client

    fun toEntity(registrationRequestDto: RegistrationRequestDto) : Client

    @Mapping(target = "id", ignore = true)
    fun updateEntityFromRequestDto(clientRequestDto: ClientRequestDto, @MappingTarget client: Client)

}