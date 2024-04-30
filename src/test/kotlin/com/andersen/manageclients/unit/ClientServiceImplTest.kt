package com.andersen.manageclients.unit

import com.andersen.manageclients.BaseUnitTest
import com.andersen.manageclients.Constants.Companion.EMAIL
import com.andersen.manageclients.Constants.Companion.FIRST_NAME
import com.andersen.manageclients.Constants.Companion.GENDER
import com.andersen.manageclients.Constants.Companion.ID
import com.andersen.manageclients.Constants.Companion.LAST_NAME
import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.mapper.ClientMapper
import com.andersen.manageclients.model.Client
import com.andersen.manageclients.model.ClientPageResponseDto
import com.andersen.manageclients.model.ClientRequestDto
import com.andersen.manageclients.model.ClientResponseDto
import com.andersen.manageclients.model.Gender
import com.andersen.manageclients.model.PageableRequest
import com.andersen.manageclients.model.SearchCriteria
import com.andersen.manageclients.model.external.genderize.GenderizeResponse
import com.andersen.manageclients.repository.ClientRepository
import com.andersen.manageclients.specification.ClientSpecification
import com.andersen.manageclients.service.GenderizeService
import com.andersen.manageclients.service.impl.ClientServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.junit.experimental.runners.Enclosed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.eq
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.only
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*


@RunWith(Enclosed::class)
class ClientServiceImplTest : BaseUnitTest() {

    @Mock
    private lateinit var clientMapper: ClientMapper

    @Mock
    private lateinit var clientRepository: ClientRepository

    @Mock
    private lateinit var genderizeService: GenderizeService

    @Mock
    private lateinit var clientSpecification: ClientSpecification

    @InjectMocks
    private lateinit var clientService: ClientServiceImpl

    private lateinit var client: Client
    private lateinit var clientResponseDto: ClientResponseDto
    private lateinit var clientRequestDto: ClientRequestDto
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

        genderizeResponse = GenderizeResponse(
            count = 1000,
            name = FIRST_NAME,
            gender = GENDER.name,
            probability = 1.0
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
        fun `getAll with search criteria should return list of clients when valid request`() {
            val listOfFullNames: MutableList<Pair<String, String>> = mutableListOf(
                Pair(first = "Lady", second = "Gaga"),
                Pair(first = "Lad", second = "gaga"),
                Pair(first = "La", second = "CumberGaga"),
                Pair(first = "L", second = "CumberGa"),
                Pair(first = "Lady", second = "G"),
                Pair(first = "Benedict", second = "Ga"),
                Pair(first = "Vadzim", second = "ga"),
                Pair(first = "Belady", second = "CumberGabatch"),
                Pair(first = "BeLady", second = "LaGa"),
                Pair(first = "Gaga", second = "Lady")
            )
            for (i in 1..10) {
                listOfFullNames.add(0, Pair(first = "Lady", second = "Gaga"))
            }
            val clientList: MutableList<Client> = mutableListOf()
            for (i in listOfFullNames.indices) {
                clientList.add(
                    Client(
                        firstName = listOfFullNames[i].first,
                        lastName = listOfFullNames[i].second,
                        email = EMAIL,
                        gender = GENDER.value,
                        id = clientId
                    )
                )
            }
            val firstNameCriteria = "La"
            val lastNameCriteria = "Ga"
            val searchCriteria = SearchCriteria(
                firstName = firstNameCriteria,
                lastName = lastNameCriteria
            )

            val pageNumber = 0
            val pageSize = 10;
            val pageableRequest = PageableRequest(
                pageNumber = pageNumber,
                pageSize = pageSize
            )
            val listOfFilteredClients = clientList.stream()
                .filter { client ->
                    client.firstName.contains(firstNameCriteria) &&
                            client.lastName.contains(lastNameCriteria)
                }
                .skip(10)
                .toList()
            val listOfFilteredClientResponseDto = listOfFilteredClients.stream()
                .map { client ->
                    ClientResponseDto(
                        firstName = client.firstName,
                        lastName = client.lastName,
                        email = client.email,
                        gender = Gender.male,
                        id = clientId.toString()
                    )
                }
                .toList()

            val pageable = PageRequest.of(pageableRequest.pageNumber, pageableRequest.pageSize)
            val page: Page<Client> = PageImpl(
                listOfFilteredClients,
                pageable,
                (clientList.size).toLong()
            )
            val expectedResult = ClientPageResponseDto(
                pageNumber = pageableRequest.pageNumber,
                elementsPerPage = pageableRequest.pageSize,
                totalPages = page.totalPages,
                totalElements = page.totalElements.toInt(),
                content = listOfFilteredClientResponseDto
            )

            `when`(clientRepository.findAll(any(), eq(pageable))).thenReturn(page)
            for (i in listOfFilteredClients.indices) {
                `when`(clientMapper.toResponseDto(listOfFilteredClients[i])).thenReturn(listOfFilteredClientResponseDto[i])
            }

            assertEquals(expectedResult, clientService.getAll(pageableRequest, searchCriteria))

            verify(clientRepository, only()).findAll(any(), eq(pageable))
        }
    }

    @Nested
    @DisplayName("Client update unit tests")
    inner class UpdateClientTests {

        @Test
        fun `update client should return updated client when client is updated`() {
            `when`(clientRepository.existsByEmail(clientRequestDto.email)).thenReturn(false)
            `when`(clientRepository.findById(clientId)).thenReturn(Optional.of(client))
            doNothing().`when`(clientMapper).updateEntityFromRequestDto(clientRequestDto, client)
            `when`(clientMapper.toResponseDto(client)).thenReturn(clientResponseDto)

            assertEquals(clientResponseDto, clientService.update(clientId, clientRequestDto))

            verify(clientRepository, times(1)).existsByEmail(clientRequestDto.email)
            verify(clientRepository, times(1)).findById(clientId)
            verify(clientMapper, times(1)).toResponseDto(client)
            verify(clientMapper, times(1)).updateEntityFromRequestDto(clientRequestDto, client)
            verifyNoMoreInteractions(clientRepository, clientMapper)
        }

        @Test
        fun `update client should throw EntityDuplicationException when email exists`() {
            `when`(clientRepository.existsByEmail(clientRequestDto.email)).thenReturn(true)

            assertThrows<EntityDuplicationException> { clientService.update(clientId, clientRequestDto) }

            verify(clientRepository, only()).existsByEmail(clientRequestDto.email)
            verifyNoMoreInteractions(clientRepository, clientMapper)
        }

        @Test
        fun `update client should throw EntityNotFoundException when clients not found by id`() {
            `when`(clientRepository.existsByEmail(clientRequestDto.email)).thenReturn(false)
            `when`(clientRepository.findById(clientId)).thenReturn(Optional.empty())

            assertThrows<EntityNotFoundException> { clientService.update(clientId, clientRequestDto) }

            verify(clientRepository, times(1)).existsByEmail(clientRequestDto.email)
            verify(clientRepository, times(1)).findById(clientId)
            verifyNoMoreInteractions(clientRepository, clientMapper)
        }
    }

    @Nested
    @DisplayName("Client delete unit tests")
    inner class DeleteClientTest {

        @Test
        fun `deleteById should delete client when client exists`() {
            `when`(clientRepository.existsById(clientId)).thenReturn(true)
            doNothing().`when`(clientRepository).deleteById(clientId)

            clientService.deleteById(clientId)

            verify(clientRepository, times(1)).existsById(clientId)
            verify(clientRepository, times(1)).deleteById(clientId)
        }

        @Test
        fun `deleteById should throw EntityNotFoundException when client not found by id`() {
            `when`(clientRepository.existsById(clientId)).thenReturn(false)

            assertThrows<EntityNotFoundException> { clientService.deleteById(clientId) }

            verify(clientRepository, only()).existsById(clientId)
            verifyNoMoreInteractions(clientRepository)
        }
    }
}