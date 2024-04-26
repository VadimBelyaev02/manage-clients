package com.andersen.manageclients.delegate

import com.andersen.manageclients.service.ClientService
import org.openapitools.client.api.ClientsApiController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ClientApiDelegateImpl(
        private val clientService: ClientService
) : ClientsApiController() {


//    override fun getClient(clientId: String): ResponseEntity<ClientResponseDto> {
//        val clientUuid= UUID.fromString(clientId);
//        return ResponseEntity.status(HttpStatus.OK).body(clientService.getById(clientUuid))
//    }

    override fun getAllClients(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body("aaa")
    }
}