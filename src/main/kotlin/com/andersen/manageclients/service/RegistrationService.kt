package com.andersen.manageclients.service

import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.RegistrationRequestDto

interface RegistrationService {

    fun register(registrationRequestDto: RegistrationRequestDto) : ClientResponseDto
}