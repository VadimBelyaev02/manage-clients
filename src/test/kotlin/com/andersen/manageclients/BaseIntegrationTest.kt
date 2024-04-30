package com.andersen.manageclients

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc

//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("integration-test")
//@ContextConfiguration(initializers = [DatabaseContainersInitializer::class])
//@AutoConfigureMockMvc

@SpringJUnitConfig
@ContextConfiguration(initializers = [DatabaseContainersInitializer::class])
@AutoConfigureMockMvc
open class BaseIntegrationTest(

) {
    protected val objectMapper = ObjectMapper()

    @Autowired
    protected lateinit var mockMvc: MockMvc

}