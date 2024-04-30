package com.andersen.manageclients.service

import com.andersen.manageclients.model.ClientPageResponseDto
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.PageableRequest
import com.andersen.manageclients.model.SearchCriteria
import org.springframework.security.access.prepost.PreAuthorize
import java.util.*

interface ClientService {

    fun getById(id: UUID): ClientResponseDto?

    fun getAll(pageable: PageableRequest, searchCriteria: SearchCriteria): ClientPageResponseDto

    fun save(clientRequestDto: ClientRequestDto): ClientResponseDto

    fun update(id: UUID, clientRequestDto: ClientRequestDto): ClientResponseDto

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    fun deleteById(id: UUID)
}