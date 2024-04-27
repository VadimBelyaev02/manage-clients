package com.andersen.manageclients.mapper.impl

import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.Client
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import org.springframework.stereotype.Component

@Component
class ClientMapperImpl : ClientMapper {
    override fun toResponseDto(client: Client): ClientResponseDto {
        return ClientResponseDto(
                id = client.id.toString(),
                firstName = client.firstName,
                lastName = client.lastName,
                email = client.email
        )
    }

    override fun toEntity(clientRequestDto: ClientRequestDto): Client {
        return Client(
                firstName = clientRequestDto.firstName,
                lastName = clientRequestDto.lastName,
                email = clientRequestDto.email,
                gender = clientRequestDto.gender.value
        )
    }

    override fun updateEntityFromRequestDto(clientRequestDto: ClientRequestDto, client: Client) {

    }
}