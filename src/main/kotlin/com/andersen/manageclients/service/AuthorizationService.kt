package com.andersen.manageclients.service

import com.andersen.manageclients.model.AuthorizationRequestDto
import com.andersen.manageclients.model.AuthorizationResponseDto

interface AuthorizationService {

    fun login(authorizationRequestDto: AuthorizationRequestDto) : AuthorizationResponseDto
}