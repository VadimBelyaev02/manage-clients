package com.andersen.manageclients.integration

import com.andersen.manageclients.BaseIntegrationTest
import com.andersen.manageclients.model.Client
import com.andersen.manageclients.repository.ClientRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ClientApiTest(
    private val clientRepository: ClientRepository
)  {

    private lateinit var client: Client


}