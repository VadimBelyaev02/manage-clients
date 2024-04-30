package com.andersen.manageclients.service.impl

import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.ClientPageResponseDto
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.PageableRequest
import com.andersen.manageclients.model.SearchCriteria
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.repository.specification.ClientSpecification
import com.andersen.manageclients.service.ClientService
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ClientServiceImpl(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper,
    private val clientSpecification: ClientSpecification
) : ClientService {

    override fun getById(id: UUID): ClientResponseDto? {
        val client = clientRepository.findById(id).orElseThrow {
            EntityNotFoundException("Client with id $id not found")
        }
        return clientMapper.toResponseDto(client)
    }

    override fun getAll(pageableRequest: PageableRequest, searchCriteria: SearchCriteria): ClientPageResponseDto {
        val pageable = PageRequest.of(pageableRequest.pageNumber, pageableRequest.pageSize)

        val page = clientRepository.findAll(
            clientSpecification.firstNameAndEmailAndLastNameLike(
                searchCriteria.firstName,
                searchCriteria.lastName
            ),
            pageable
        )

        return ClientPageResponseDto(
            pageNumber = pageableRequest.pageNumber,
            elementsPerPage = pageableRequest.pageSize,
            totalPages = page.totalPages,
            totalElements = page.totalElements.toInt(),
            content = page.content.map { clientMapper.toResponseDto(it) }
        )
    }


    @Transactional
    override fun update(id: UUID, clientRequestDto: ClientRequestDto): ClientResponseDto {
        if (clientRepository.existsByEmail(clientRequestDto.email)) {
            throw EntityDuplicationException("Client with email = ${clientRequestDto.email} already exists")
        }
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
}