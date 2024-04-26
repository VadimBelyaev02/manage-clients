package com.andersen.manageclients.service.impl

import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.service.ClientService
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper
) : ClientService {

    override fun getById(id: UUID): ClientResponseDto? {
        val client = clientRepository.findById(id).orElseThrow {
            EntityNotFoundException("Client with id $id not found")
        }
        return clientMapper.toResponseDto(client)
    }

    override fun getAll(): List<ClientResponseDto> {
        val clients = clientRepository.findAll()
        return clients.map { clientMapper.toResponseDto(it) }
    }

    override fun save(clientRequestDto: ClientRequestDto): ClientResponseDto {
        checkUniqueness(clientRequestDto)

        val client = clientMapper.toEntity(clientRequestDto)
        val savedClient = clientRepository.save(client)
        return clientMapper.toResponseDto(savedClient)
    }

    override fun update(id: UUID, clientRequestDto: ClientRequestDto): ClientResponseDto {
        checkUniqueness(clientRequestDto)

        val client = clientRepository.findById(id).orElseThrow {
            EntityNotFoundException("Client with id = $id not found")
        }
        clientMapper.updateEntityFromRequestDto(clientRequestDto, client)
        val updatedClient = clientRepository.save(client)
        return clientMapper.toResponseDto(updatedClient)
    }

    override fun deleteById(id: UUID) {
        if (!clientRepository.existsById(id)) {
            throw EntityNotFoundException("Client with id $id not found")
        }
        clientRepository.deleteById(id);
    }

    private fun checkUniqueness(clientRequestDto: ClientRequestDto) {
        if (clientRepository.existsByEmail(clientRequestDto.email)) {
            throw EntityDuplicationException("Client with email = ${clientRequestDto.email} already exists")
        }
    }
}