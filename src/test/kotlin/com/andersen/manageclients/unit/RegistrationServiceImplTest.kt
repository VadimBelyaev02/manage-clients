package com.andersen.manageclients.unit

import com.andersen.manageclients.BaseUnitTest
import com.andersen.manageclients.Constants.Companion.EMAIL
import com.andersen.manageclients.Constants.Companion.FIRST_NAME
import com.andersen.manageclients.Constants.Companion.GENDER
import com.andersen.manageclients.Constants.Companion.ID
import com.andersen.manageclients.Constants.Companion.LAST_NAME
import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.exception.GenderProbabilityException
import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.Client
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.Gender
import com.andersen.manageclients.model.PageableRequest
import com.andersen.manageclients.model.RegistrationRequestDto
import com.andersen.manageclients.model.SearchCriteria
import com.andersen.manageclients.model.external.genderize.GenderizeResponse
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.specification.ClientSpecification
import com.andersen.manageclients.service.GenderizeService
import com.andersen.manageclients.service.impl.ClientServiceImpl
import com.andersen.manageclients.service.impl.RegistrationServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.junit.experimental.runners.Enclosed
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.only
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class RegistrationServiceImplTest : BaseUnitTest() {

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Mock
    private lateinit var clientMapper: ClientMapper

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var genderizeService: GenderizeService

    @InjectMocks
    private lateinit var registrationService: RegistrationServiceImpl


    private lateinit var client: Client
    private lateinit var clientResponseDto: ClientResponseDto
    private lateinit var registrationRequestDto: RegistrationRequestDto
    private lateinit var genderizeResponse: GenderizeResponse
    private val clientId = ID

    @BeforeEach
    fun setUp() {
        client = Client(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            gender = GENDER.value,
            id = clientId
        )

        registrationRequestDto = RegistrationRequestDto(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            gender = GENDER,
            password = "password"
        )

        clientResponseDto = ClientResponseDto(
            firstName = FIRST_NAME,
            lastName = LAST_NAME,
            email = EMAIL,
            gender = GENDER,
            id = clientId.toString()
        )

        genderizeResponse = GenderizeResponse(
            count = 1000,
            name = FIRST_NAME,
            gender = GENDER.name,
            probability = 1.0
        )
    }
    @Test
    fun `register client should throw EntityDuplicationException when email exists`() {
        `when`(clientRepository.existsByEmail(registrationRequestDto.email)).thenReturn(true)

        assertThrows<EntityDuplicationException> { registrationService.register(registrationRequestDto) }

        verify(clientRepository, only()).existsByEmail(registrationRequestDto.email)
        verifyNoMoreInteractions(clientRepository, clientMapper)
    }

    @Test
    fun `register client should returned saved client when clients is saved`() {
        `when`(clientRepository.existsByEmail(registrationRequestDto.email)).thenReturn(false)
        `when`(genderizeService.determineGenderProbability(registrationRequestDto.firstName)).thenReturn(genderizeResponse)
        `when`(clientMapper.toEntity(registrationRequestDto)).thenReturn(client)
        `when`(clientRepository.save(client)).thenReturn(client)
        `when`(clientMapper.toResponseDto(client)).thenReturn(clientResponseDto)

        assertEquals(clientResponseDto, registrationService.register(registrationRequestDto))

        verify(clientRepository, times(1)).existsByEmail(registrationRequestDto.email)
        verify(genderizeService, only()).determineGenderProbability(registrationRequestDto.firstName)
        verify(clientMapper, times(1)).toEntity(registrationRequestDto)
        verify(clientRepository, times(1)).save(client)
        verify(clientMapper, times(1)).toResponseDto(client)
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.1, 0.2, 0.5, 0.7999999999999, 0.8, 0.8000000000001, 0.9, 1.0])
    fun `register client with different name probabilities`(probability: Double) {
        val genderizeResp = GenderizeResponse(
            count = 1000,
            name = FIRST_NAME,
            gender = GENDER.name,
            probability = probability
        )

        `when`(clientRepository.existsByEmail(registrationRequestDto.email)).thenReturn(false)
        `when`(genderizeService.determineGenderProbability(registrationRequestDto.firstName)).thenReturn(genderizeResp)

        if (probability < 0.8) {
            assertThrows<GenderProbabilityException> { registrationService.register(registrationRequestDto) }
        } else {
            `when`(clientMapper.toEntity(registrationRequestDto)).thenReturn(client)
            `when`(clientRepository.save(client)).thenReturn(client)
            `when`(clientMapper.toResponseDto(client)).thenReturn(clientResponseDto)

            assertDoesNotThrow { registrationService.register(registrationRequestDto) }

            verify(clientMapper, times(1)).toEntity(registrationRequestDto)
            verify(clientRepository, times(1)).save(client)
            verify(clientMapper, times(1)).toResponseDto(client)
        }

        verify(clientRepository, times(1)).existsByEmail(registrationRequestDto.email)
        verify(genderizeService, only()).determineGenderProbability(registrationRequestDto.firstName)
        verifyNoMoreInteractions(clientRepository, genderizeService, clientMapper)
    }

}