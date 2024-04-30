//package com.andersen.manageclients.integration
//
//import com.andersen.manageclients.BaseIntegrationTest
//import org.junit.jupiter.api.Test
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import org.springframework.transaction.annotation.Transactional
//import org.testcontainers.shaded.org.hamcrest.Matchers.greaterThan
//import org.springframework.security.access.AccessDeniedException
//import org.springframework.security.test.context.support.WithMockUser
//import org.springframework.test.annotation.Rollback
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import org.junit.jupiter.api.Assertions.assertInstanceOf
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//import org.testcontainers.shaded.org.hamcrest.Matchers.greaterThan
//import org.testcontainers.shaded.org.hamcrest.Matchers.hasSize
//
//
//class ClientApiTest : BaseIntegrationTest() {
//
//
//    private val PATH_PREFIX = "/api/v1/clients"
//
////    @Test
////    @Transactional
////    fun `getAllClients should return a list of clients`() {
////        mockMvc.perform(MockMvcRequestBuilders.get(PATH_PREFIX))
////            .andExpect(status().isOk)
////            // .andExpect(jsonPath("$", hasSize(greaterThan(0))).hasJsonPath());
////    }
//
//}