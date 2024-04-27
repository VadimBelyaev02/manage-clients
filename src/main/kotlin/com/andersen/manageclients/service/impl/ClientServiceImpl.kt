package com.andersen.manageclients.service.impl

import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.exception.GenderProbabilityException
import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.service.ClientService
import com.andersen.manageclients.service.GenderizeService
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper,
    private val genderizeService: GenderizeService
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

    @Transactional
    override fun save(clientRequestDto: ClientRequestDto): ClientResponseDto {
        checkUniqueness(clientRequestDto)

        val genderProbability = genderizeService.determineGenderProbability(clientRequestDto.firstName)
        if (genderProbability != null && genderProbability < 0.8) {
            throw GenderProbabilityException("Gender not detected")
        }

        val client = clientMapper.toEntity(clientRequestDto)
        val savedClient = clientRepository.save(client)
        return clientMapper.toResponseDto(savedClient)
    }

    @Transactional
    override fun update(id: UUID, clientRequestDto: ClientRequestDto): ClientResponseDto {
        checkUniqueness(clientRequestDto)

        val client = clientRepository.findById(id).orElseThrow {
            EntityNotFoundException("Client with id = $id not found")
        }
        clientMapper.updateEntityFromRequestDto(clientRequestDto, client)
        return clientMapper.toResponseDto(client)
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