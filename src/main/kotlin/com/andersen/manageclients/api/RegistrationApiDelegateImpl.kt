package com.andersen.manageclients.api

import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.RegistrationRequestDto
import com.andersen.manageclients.service.RegistrationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class RegistrationApiDelegateImpl(
    private val registrationService: RegistrationService
) : RegistrationApiDelegate {

    override fun registerClient(registrationRequestDto: RegistrationRequestDto): ResponseEntity<ClientResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(registrationService.register(registrationRequestDto))
    }
}