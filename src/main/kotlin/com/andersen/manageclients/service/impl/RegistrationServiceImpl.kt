package com.andersen.manageclients.service.impl

import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.exception.GenderProbabilityException
import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.RegistrationRequestDto
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.service.GenderizeService
import com.andersen.manageclients.service.RegistrationService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegistrationServiceImpl(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper,
    private val passwordEncoder: PasswordEncoder,
    private val genderizeService: GenderizeService
) : RegistrationService {


    @Transactional
    override fun register(registrationRequestDto: RegistrationRequestDto): ClientResponseDto {
        if (clientRepository.existsByEmail(registrationRequestDto.email)) {
            throw EntityDuplicationException("Client with email ${registrationRequestDto.email} already exists")
        }
        val genderizeResponse = genderizeService.determineGenderProbability(registrationRequestDto.firstName)
        val probability = genderizeResponse.probability
        val gendersEqual = genderizeResponse.gender == registrationRequestDto.gender?.name
        if ((gendersEqual && probability < 0.8) || (!gendersEqual && probability >= 0.2)) {
            throw GenderProbabilityException("Gender not detected")
        }
        val client = clientMapper.toEntity(registrationRequestDto)
        client.password = passwordEncoder.encode(client.password)

        val savedClient = clientRepository.save(client)

        return clientMapper.toResponseDto(savedClient)
    }
}