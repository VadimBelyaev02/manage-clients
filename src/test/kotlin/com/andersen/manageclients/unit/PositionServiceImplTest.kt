package com.andersen.manageclients.unit

import com.andersen.manageclients.BaseUnitTest
import com.andersen.manageclients.Constants.Companion.ID
import com.andersen.manageclients.Constants.Companion.JOB_NAME
import com.andersen.manageclients.Constants.Companion.POSITION_NAME
import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.mapper.JobMapper
import com.andersen.manageclients.mapper.PositionMapper
import com.andersen.manageclients.model.Job
import com.andersen.manageclients.model.JobPosition
import com.andersen.manageclients.model.JobPositionId
import com.andersen.manageclients.model.JobRequestDto
import com.andersen.manageclients.model.JobResponseDto
import com.andersen.manageclients.model.Position
import com.andersen.manageclients.model.PositionRequestDto
import com.andersen.manageclients.model.PositionResponseDto
import com.andersen.manageclients.repository.JobRepository
import com.andersen.manageclients.repository.PositionRepository
import com.andersen.manageclients.service.impl.JobServiceImpl
import com.andersen.manageclients.service.impl.PositionServiceImpl
import jakarta.persistence.EntityNotFoundException
import org.junit.experimental.runners.Enclosed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.only
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import java.util.*
import kotlin.collections.List


@RunWith(Enclosed::class)
class PositionServiceImplTest : BaseUnitTest() {

    @Mock
    private lateinit var positionMapper: PositionMapper

    @Mock
    private lateinit var positionRepository: PositionRepository

    @Mock
    private lateinit var jobRepository: JobRepository

    @InjectMocks
    private lateinit var positionService: PositionServiceImpl

    private lateinit var position: Position
    private lateinit var positionResponseDto: PositionResponseDto
    private lateinit var positionRequestDto: PositionRequestDto
    private lateinit var job: Job

    private val positionId = ID

    @BeforeEach
    fun setUp() {
        position = Position(
            id = positionId,
            name = JOB_NAME,
            positionJobs = emptyList()
        )

        job = Job(
            id = ID,
            name = JOB_NAME
        )

        val jobResponseDto = JobResponseDto(
            id = ID.toString(),
            name = JOB_NAME
        )

        val jobPosition = JobPosition(
            job = job,
            position = position
        )

        position.positionJobs = listOf(jobPosition)


        positionRequestDto = PositionRequestDto(
            name = POSITION_NAME,
            jobIds = listOf(ID.toString())
        )

        positionResponseDto = PositionResponseDto(
            id = positionId.toString(),
            name = POSITION_NAME,
            jobs = listOf(jobResponseDto)
        )
    }


    @Nested
    @DisplayName("Position get unit tests")
    inner class GetJobTests {

        @Test
        fun `getById should return client when job is found`() {
            `when`(positionRepository.findById(positionId)).thenReturn(Optional.of(position))
            `when`(positionMapper.toResponseDto(position)).thenReturn(positionResponseDto)

            assertEquals(positionResponseDto, positionService.getById(positionId))

            verify(positionRepository, only()).findById(positionId)
            verify(positionMapper, only()).toResponseDto(position)
        }

        @Test
        fun `getById should throw EntityNotFoundException when job not found`() {
            `when`(positionRepository.findById(positionId)).thenReturn(Optional.empty())

            assertThrows<EntityNotFoundException> { positionService.getById(positionId) }

            verify(positionRepository, only()).findById(positionId)
        }

        @Test
        fun `getAll should return list of jobs when jobs exist`() {
            val positions = listOf(position, position, position)
            val expectedResult = listOf(positionResponseDto, positionResponseDto, positionResponseDto)

            `when`(positionRepository.findAll()).thenReturn(positions)
            `when`(positionMapper.toResponseDto(position)).thenReturn(positionResponseDto)

            assertEquals(expectedResult, positionService.getAll())

            verify(jobRepository, only()).findAll()
            verify(positionMapper, times(expectedResult.size)).toResponseDto(position)
        }

        @Test
        fun `getAll should return empty list when no jobs`() {
            val positions = listOf<Position>()
            val expectedResult = listOf<PositionResponseDto>()

            `when`(positionRepository.findAll()).thenReturn(positions)

            assertEquals(expectedResult, positionService.getAll())

            verify(positionRepository, only()).findAll()
            verifyNoInteractions(positionMapper)
        }
    }

    @Nested
    @DisplayName("Position save unit tests")
    inner class SavePositionTests {

        @Test
        fun `save position should throw EntityDuplicationException when name exists`() {
            `when`(positionRepository.existsByName(positionRequestDto.name)).thenReturn(true)

            assertThrows<EntityDuplicationException> { positionService.save(positionRequestDto) }

            verify(positionRepository, only()).existsByName(positionRequestDto.name)
            verifyNoMoreInteractions(positionRepository, positionMapper)
        }

        @Test
        fun `save position should throw EntityNotFoundException when job not found by id`() {
            val jobIds = positionRequestDto.jobIds.map { UUID.fromString(it) }
            val jobs = emptyList<Job>()

            `when`(positionRepository.existsByName(positionRequestDto.name)).thenReturn(false)
            `when`(positionMapper.toEntity(positionRequestDto)).thenReturn(position)
            `when`(jobRepository.findAllByJobIds(jobIds)).thenReturn(jobs)

            assertThrows<EntityNotFoundException> { positionService.save(positionRequestDto) }

            verify(positionRepository, only()).existsByName(positionRequestDto.name)
            verify(jobRepository, only()).findAllByJobIds(jobIds)
            verify(positionMapper, only()).toEntity(positionRequestDto)
        }


        @Test
        fun `save position should returned saved position when valid request`() {
            val jobIds = positionRequestDto.jobIds.map { UUID.fromString(it) }
            val jobs = listOf(job)

            `when`(positionRepository.existsByName(positionRequestDto.name)).thenReturn(false)
            `when`(jobRepository.findAllByJobIds(jobIds)).thenReturn(jobs)
            `when`(positionMapper.toEntity(positionRequestDto)).thenReturn(position)
            `when`(positionRepository.save(position)).thenReturn(position)
            `when`(positionMapper.toResponseDto(position)).thenReturn(positionResponseDto)

            assertEquals(positionResponseDto, positionService.save(positionRequestDto))

            verify(positionRepository, times(1)).existsByName(positionRequestDto.name)
            verify(jobRepository, only()).findAllByJobIds(jobIds)
            verify(positionMapper, times(1)).toEntity(positionRequestDto)
            verify(positionRepository, times(1)).save(position)
            verify(positionMapper, times(1)).toResponseDto(position)
        }
    }

    @Nested
    @DisplayName("Position update unit tests")
    inner class UpdatePositionTests {

        @Test
        fun `update position should return updated position when valid request`() {
            val jobIds = positionRequestDto.jobIds.map { UUID.fromString(it) }
            val jobs = listOf(job)

            `when`(positionRepository.existsByName(positionRequestDto.name)).thenReturn(false)
            `when`(positionRepository.findById(positionId)).thenReturn(Optional.of(position))
            doNothing().`when`(positionMapper).updateEntityFromRequestDto(positionRequestDto, position)
            `when`(positionMapper.toResponseDto(position)).thenReturn(positionResponseDto)
            `when`(jobRepository.findAllByJobIds(jobIds)).thenReturn(jobs)

            assertEquals(positionResponseDto, positionService.update(positionId, positionRequestDto))

            verify(positionRepository, times(1)).existsByName(positionRequestDto.name)
            verify(positionRepository, times(1)).findById(positionId)
            verify(positionMapper, times(1)).toResponseDto(position)
            verify(positionMapper, times(1)).updateEntityFromRequestDto(positionRequestDto, position)
            verify(jobRepository, only()).findAllByJobIds(jobIds)
            verifyNoMoreInteractions(positionRepository, positionMapper)
        }

        @Test
        fun `update position should throw EntityNotFoundException when job not found by id`() {
            val jobIds = positionRequestDto.jobIds.map { UUID.fromString(it) }
            val jobs = emptyList<Job>()

            `when`(positionRepository.findById(positionId)).thenReturn(Optional.of(position))
            `when`(positionRepository.existsByName(positionRequestDto.name)).thenReturn(false)
            doNothing().`when`(positionMapper).updateEntityFromRequestDto(positionRequestDto, position)
            `when`(jobRepository.findAllByJobIds(jobIds)).thenReturn(jobs)

            assertThrows<EntityNotFoundException> { positionService.update(positionId, positionRequestDto) }

            verify(positionRepository, times(1)).findById(positionId)
            verify(positionRepository, times(1)).existsByName(positionRequestDto.name)
            verify(jobRepository, only()).findAllByJobIds(jobIds)
            verify(positionMapper, only()).updateEntityFromRequestDto(positionRequestDto, position)
        }

        @Test
        fun `update job should throw EntityDuplicationException when name exists`() {
            `when`(positionRepository.existsByName(positionRequestDto.name)).thenReturn(true)

            assertThrows<EntityDuplicationException> { positionService.update(positionId, positionRequestDto) }

            verify(positionRepository, only()).existsByName(positionRequestDto.name)
            verifyNoMoreInteractions(positionRepository, positionMapper)
        }

        @Test
        fun `update position should throw EntityNotFoundException when position not found by id`() {
            `when`(positionRepository.existsByName(positionRequestDto.name)).thenReturn(false)
            `when`(positionRepository.findById(positionId)).thenReturn(Optional.empty())

            assertThrows<EntityNotFoundException> { positionService.update(positionId, positionRequestDto) }

            verify(positionRepository, times(1)).existsByName(positionRequestDto.name)
            verify(positionRepository, times(1)).findById(positionId)
            verifyNoMoreInteractions(positionRepository, positionMapper)
        }
    }

    @Nested
    @DisplayName("Position delete unit tests")
    inner class DeletePositionTests {

        @Test
        fun `deleteById should delete position when position exists`() {
            `when`(positionRepository.existsById(positionId)).thenReturn(true)
            doNothing().`when`(positionRepository).deleteById(positionId)

            positionService.deleteById(positionId)

            verify(positionRepository, times(1)).existsById(positionId)
            verify(positionRepository, times(1)).deleteById(positionId)
        }

        @Test
        fun `deleteById should throw EntityNotFoundException when position not found by id`() {
            `when`(positionRepository.existsById(positionId)).thenReturn(false)

            assertThrows<EntityNotFoundException> { positionService.deleteById(positionId) }

            verify(positionRepository, only()).existsById(positionId)
            verifyNoMoreInteractions(positionRepository)
        }
    }
}