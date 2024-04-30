package com.andersen.manageclients.unit

import com.andersen.manageclients.BaseUnitTest
import com.andersen.manageclients.Constants
import com.andersen.manageclients.Constants.Companion.EMAIL
import com.andersen.manageclients.Constants.Companion.ID
import com.andersen.manageclients.Constants.Companion.PASSWORD
import com.andersen.manageclients.Constants.Companion.TOKEN
import com.andersen.manageclients.config.security.JwtTokenProvider
import com.andersen.manageclients.exception.AuthorizationException
import com.andersen.manageclients.model.AuthorizationRequestDto
import com.andersen.manageclients.model.AuthorizationResponseDto
import com.andersen.manageclients.model.Client
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.service.impl.AuthorizationServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.only
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class AuthorizationServiceImplTest : BaseUnitTest(){

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Mock
    private lateinit var tokenProvider: JwtTokenProvider

    @InjectMocks
    private lateinit var authorizationService: AuthorizationServiceImpl

    private lateinit var authorizationRequestDto: AuthorizationRequestDto
    private lateinit var authorizationResponseDto: AuthorizationResponseDto
    private lateinit var client: Client


    @BeforeEach
    fun setUp() {
        authorizationResponseDto = AuthorizationResponseDto(
            email = EMAIL,
            token = TOKEN
        )

        authorizationRequestDto = AuthorizationRequestDto(
            email = EMAIL,
            password = PASSWORD
        )

        client = Client(
            firstName = Constants.FIRST_NAME,
            lastName = Constants.LAST_NAME,
            email = EMAIL,
            gender = Constants.GENDER.value,
            id = ID
        )
    }

    @Test
    fun `login should throw EntityNotFoundException when client not found by id`() {
        `when`(clientRepository.findByEmail(authorizationRequestDto.email)).thenReturn(Optional.empty<Client>())

        assertThrows<EntityNotFoundException> { authorizationService.login(authorizationRequestDto) }

        verify(clientRepository, only()).findByEmail(authorizationRequestDto.email)
        verifyNoInteractions(passwordEncoder, tokenProvider)
    }

    @Test
    fun `login should throw AuthorizationException when invalid password`() {
        `when`(clientRepository.findByEmail(authorizationRequestDto.email)).thenReturn(Optional.of(client))
        `when`(passwordEncoder.matches(authorizationRequestDto.password, client.password)).thenReturn(false)

        assertThrows<AuthorizationException> { authorizationService.login(authorizationRequestDto) }

        verify(clientRepository, only()).findByEmail(authorizationRequestDto.email)
        verify(passwordEncoder, only()).matches(authorizationRequestDto.password, client.password)
        verifyNoInteractions(tokenProvider)
    }

    @Test
    fun `login should return authorization response when valid request`() {
        val role = "CLIENT"

        `when`(clientRepository.findByEmail(authorizationRequestDto.email)).thenReturn(Optional.of(client))
        `when`(passwordEncoder.matches(authorizationRequestDto.password, client.password)).thenReturn(true)
        `when`(tokenProvider.createToken(authorizationRequestDto.email, role)).thenReturn(TOKEN)

        assertEquals(authorizationResponseDto, authorizationService.login(authorizationRequestDto))

        verify(clientRepository, only()).findByEmail(authorizationRequestDto.email)
        verify(passwordEncoder, only()).matches(authorizationRequestDto.password, client.password)
        verify(tokenProvider, only()).createToken(authorizationRequestDto.email, role)
    }
}