package com.andersen.manageclients.service

import org.openapitools.client.model.ClientRequestDto
import org.openapitools.client.model.ClientResponseDto
import java.util.*

interface ClientService {

    fun getById(id: UUID): ClientResponseDto?

    fun getAll(): List<ClientResponseDto>

    fun save(clientRequestDto: ClientRequestDto): ClientResponseDto

    fun update(id: UUID, clientRequestDto: ClientRequestDto): ClientResponseDto

    fun deleteById(id: UUID)
}