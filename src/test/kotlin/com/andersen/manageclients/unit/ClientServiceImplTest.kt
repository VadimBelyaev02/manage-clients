package com.andersen.manageclients.unit

import com.andersen.manageclients.BaseUnitTest
import com.andersen.manageclients.Constants.Companion.CLIENT_ID
import com.andersen.manageclients.Constants.Companion.EMAIL
import com.andersen.manageclients.Constants.Companion.FIRST_NAME
import com.andersen.manageclients.Constants.Companion.GENDER
import com.andersen.manageclients.Constants.Companion.LAST_NAME
import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.Client
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import org.mockito.Mock
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.service.ClientService
import com.andersen.manageclients.service.GenderizeService
import com.andersen.manageclients.service.impl.ClientServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.junit.Before
import org.junit.BeforeClass
import org.junit.experimental.runners.Enclosed
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.ArrayList
import org.junit.jupiter.api.Test;

import java.util.Optional
import java.util.UUID
import java.util.stream.Collectors
import java.util.stream.Stream
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.api.Assertions.assertThrows

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*


@RunWith(Enclosed::class)
class ClientServiceImplTest : BaseUnitTest() {

    @Mock
    private lateinit var clientMapper: ClientMapper

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Mock
    private lateinit var genderizeService: GenderizeService

    @InjectMocks
    private lateinit var clientService: ClientServiceImpl

    private lateinit var client: Client
    private lateinit var clientResponseDto: ClientResponseDto
    private lateinit var clientRequestDto: ClientRequestDto
    private val clientId = CLIENT_ID

    @BeforeEach
    fun setUp() {
        client = Client(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            gender = GENDER.value,
            id = clientId
        )

        clientRequestDto = ClientRequestDto(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            gender = GENDER
        )

        clientResponseDto = ClientResponseDto(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            gender = GENDER,
            id = clientId.toString()
        )
    }


    @Nested
    @DisplayName("Client get unit tests")
    inner class GetClientTests {

        @Test
        fun `getById should return client when client is found`() {
            `when`(clientRepository.findById(clientId)).thenReturn(Optional.of(client))
            `when`(clientMapper.toResponseDto(client)).thenReturn(clientResponseDto)

            assertEquals(clientResponseDto, clientService.getById(clientId))

            verify(clientRepository, only()).findById(clientId)
            verify(clientMapper, only()).toResponseDto(client)
        }

        @Test
        fun `getById should throw EntityNotFoundException when client not found`() {
            `when`(clientRepository.findById(clientId)).thenReturn(Optional.empty())

            assertThrows<EntityNotFoundException> { clientService.getById(clientId) }

            verify(clientRepository, only()).findById(clientId)
        }

        @Test
        fun `getAll should return list of clients when clients exist`() {
            val clients = listOf(client, client, client)
            val expectedResult = listOf(clientResponseDto, clientResponseDto, clientResponseDto)

            `when`(clientRepository.findAll()).thenReturn(clients)
            `when`(clientMapper.toResponseDto(client)).thenReturn(clientResponseDto)

            assertEquals(expectedResult, clientService.getAll())

            verify(clientRepository, only()).findAll()
            verify(clientMapper, times(expectedResult.size)).toResponseDto(client)
        }

        @Test
        fun `getAll should return empty list when no clients`() {
            val clients = listOf<Client>()
            val expectedResult = listOf<ClientResponseDto>()

            `when`(clientRepository.findAll()).thenReturn(clients)

            assertEquals(expectedResult, clientService.getAll())

            verify(clientRepository, only()).findAll()
            verifyNoInteractions(clientMapper)
        }
    }

    @Nested
    @DisplayName("Client create unit tests")
    inner class UpdateClientTests {

        @Test
        fun `updateClient should return updated client when client is updated`() {
            `when`(clientRepository.existsByEmail(clientRequestDto.email)).thenReturn(false)
            `when`(clientRepository.findById(clientId)).thenReturn(Optional.of(client))
            doNothing().`when`(clientMapper.updateEntityFromRequestDto(clientRequestDto, client))
            `when`(clientMapper.toResponseDto(client)).thenReturn(clientResponseDto)

            assertEquals(clientResponseDto, clientService.getById(clientId))

            verify(clientRepository, times(1)).existsByEmail(clientRequestDto.email)
            verify(clientRepository, times(1)).findById(clientId)
            verify(clientMapper, times(1)).toResponseDto(client)
            verify(clientMapper, times(1)).updateEntityFromRequestDto(clientRequestDto, client)
            verifyNoMoreInteractions(clientRepository, clientMapper)
        }

        @Test
        fun `saveClient should throw EntityNotFoundException when client not found`() {
            `when`(clientRepository.findById(clientId)).thenReturn(Optional.empty())


            assertThrows<EntityNotFoundException> { clientService.getById(clientId) }

            verify(clientRepository, only()).findById(clientId)
        }

        @Test
        fun `getAll should return list of clients when clients exist`() {
            val clients = listOf(client, client, client)
            val expectedResult = listOf(clientResponseDto, clientResponseDto, clientResponseDto)

            `when`(clientRepository.findAll()).thenReturn(clients)
            `when`(clientMapper.toResponseDto(client)).thenReturn(clientResponseDto)

            assertEquals(expectedResult, clientService.getAll())

            verify(clientRepository, only()).findAll()
            verify(clientMapper, times(expectedResult.size)).toResponseDto(client)
        }

        @Test
        fun `getAll should return empty list when no clients`() {
            val clients = listOf<Client>()
            val expectedResult = listOf<ClientResponseDto>()

            `when`(clientRepository.findAll()).thenReturn(clients)

            assertEquals(expectedResult, clientService.getAll())

            verify(clientRepository, only()).findAll()
            verifyNoInteractions(clientMapper)
        }
    }
}