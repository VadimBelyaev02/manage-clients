package com.andersen.manageclients.api

import com.andersen.manageclients.model.AuthorizationRequestDto
import com.andersen.manageclients.model.AuthorizationResponseDto
import com.andersen.manageclients.service.AuthorizationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class AuthorizationApiDelegateImpl(
    private val authorizationService: AuthorizationService
): AuthorizationApiDelegate {

    override fun authorizeClient(authorizationRequestDto: AuthorizationRequestDto): ResponseEntity<AuthorizationResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(authorizationService.login(authorizationRequestDto))
    }
}