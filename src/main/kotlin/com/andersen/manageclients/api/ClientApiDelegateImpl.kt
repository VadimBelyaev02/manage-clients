package com.andersen.manageclients.api

import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.service.ClientService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClientApiDelegateImpl(
    private val clientService: ClientService
) : ClientsApiDelegate {

    override fun getClient(clientId: String): ResponseEntity<ClientResponseDto> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getById(UUID.fromString(clientId)))
    }

    override fun getAllClients(): ResponseEntity<List<ClientResponseDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.getAll())
    }

    override fun deleteClient(clientId: String): ResponseEntity<Unit> {
        clientService.deleteById(UUID.fromString(clientId))
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    override fun saveClient(clientRequestDto: ClientRequestDto): ResponseEntity<ClientResponseDto> {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(clientRequestDto));
    }

    override fun updateClient(clientId: String, clientRequestDto: ClientRequestDto): ResponseEntity<ClientResponseDto> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(clientService.update(UUID.fromString(clientId), clientRequestDto))
    }
}