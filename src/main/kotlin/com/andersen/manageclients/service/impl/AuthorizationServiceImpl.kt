package com.andersen.manageclients.service.impl

import com.andersen.manageclients.config.security.JwtTokenProvider
import com.andersen.manageclients.exception.AuthorizationException
import com.andersen.manageclients.model.AuthorizationRequestDto
import com.andersen.manageclients.model.AuthorizationResponseDto
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.service.AuthorizationService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthorizationServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val clientRepository: ClientRepository,
    private val tokenProvider: JwtTokenProvider
) : AuthorizationService {
    override fun login(authorizationRequestDto: AuthorizationRequestDto): AuthorizationResponseDto {
        val client = clientRepository.findByEmail(authorizationRequestDto.email) // client can be null

        if (!passwordEncoder.matches(authorizationRequestDto.password, client?.password)) {
            throw AuthorizationException("Invalid password")
        }

        val token = tokenProvider.createToken(authorizationRequestDto.email, "CLIENT")

        return AuthorizationResponseDto(
            email = authorizationRequestDto.email,
            token = token
        )
    }
}