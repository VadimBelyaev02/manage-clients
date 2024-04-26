package com.andersen.manageclients.mapper.impl

import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.Client
import org.openapitools.client.model.ClientRequestDto
import org.openapitools.client.model.ClientResponseDto
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
                email = clientRequestDto.email
        )
    }

    override fun updateEntityFromRequestDto(clientRequestDto: ClientRequestDto, client: Client) {

    }
}