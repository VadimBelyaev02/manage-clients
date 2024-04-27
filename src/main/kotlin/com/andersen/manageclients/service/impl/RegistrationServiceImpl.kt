package com.andersen.manageclients.service.impl

import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.RegistrationRequestDto
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.service.RegistrationService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegistrationServiceImpl(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper,
    private val passwordEncoder: PasswordEncoder
) : RegistrationService {


    override fun register(registrationRequestDto: RegistrationRequestDto): ClientResponseDto {
        if (clientRepository.existsByEmail(registrationRequestDto.email)) {
            throw EntityDuplicationException("Client with email ${registrationRequestDto.email} already exists")
        }

        val client = clientMapper.toEntity(registrationRequestDto)
        client.password = passwordEncoder.encode(client.password)

        val savedClient = clientRepository.save(client)

        return clientMapper.toResponseDto(savedClient)
    }
}