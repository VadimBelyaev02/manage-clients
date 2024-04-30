//package com.andersen.manageclients
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.junit.jupiter.api.extension.ExtendWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.ContextConfiguration
//import org.springframework.test.context.junit.jupiter.SpringExtension
//import org.springframework.test.web.servlet.MockMvc
//
//@ActiveProfiles("test")
//@ContextConfiguration(initializers = [DatabaseContainersInitializer::class])
//
//@ExtendWith(SpringExtension::class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [DatabaseContainersInitializer::class])
//@AutoConfigureMockMvc
//abstract class BaseIntegrationTest {
//
//    @Autowired
//    protected lateinit var mockMvc: MockMvc
//
//    @Autowired
//    protected lateinit var objectMapper: ObjectMapper
//
//}