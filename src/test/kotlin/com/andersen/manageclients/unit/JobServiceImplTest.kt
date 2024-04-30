package com.andersen.manageclients.unit

import com.andersen.manageclients.BaseUnitTest
import com.andersen.manageclients.Constants.Companion.ID
import com.andersen.manageclients.Constants.Companion.JOB_NAME
import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.mapper.JobMapper
import com.andersen.manageclients.model.Job
import com.andersen.manageclients.model.JobRequestDto
import com.andersen.manageclients.model.JobResponseDto
import com.andersen.manageclients.repository.JobRepository
import com.andersen.manageclients.service.impl.JobServiceImpl
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


@RunWith(Enclosed::class)
class JobServiceImplTest : BaseUnitTest() {

    @Mock
    private lateinit var jobMapper: JobMapper

    @Mock
    private lateinit var jobRepository: JobRepository

    @InjectMocks
    private lateinit var jobService: JobServiceImpl

    private lateinit var job: Job
    private lateinit var jobResponseDto: JobResponseDto
    private lateinit var jobRequestDto: JobRequestDto
    private val jobId = ID

    @BeforeEach
    fun setUp() {
        job = Job(
            id = jobId,
            name = JOB_NAME
        )

        jobRequestDto = JobRequestDto(
            name = JOB_NAME
        )

        jobResponseDto = JobResponseDto(
            id = jobId.toString(),
            name = JOB_NAME
        )
    }


    @Nested
    @DisplayName("Job get unit tests")
    inner class GetJobTests {

        @Test
        fun `getById should return client when job is found`() {
            `when`(jobRepository.findById(jobId)).thenReturn(Optional.of(job))
            `when`(jobMapper.toResponseDto(job)).thenReturn(jobResponseDto)

            assertEquals(jobResponseDto, jobService.getById(jobId))

            verify(jobRepository, only()).findById(jobId)
            verify(jobMapper, only()).toResponseDto(job)
        }

        @Test
        fun `getById should throw EntityNotFoundException when job not found`() {
            `when`(jobRepository.findById(jobId)).thenReturn(Optional.empty())

            assertThrows<EntityNotFoundException> { jobService.getById(jobId) }

            verify(jobRepository, only()).findById(jobId)
        }

        @Test
        fun `getAll should return list of jobs when jobs exist`() {
            val jobs = listOf(job, job, job)
            val expectedResult = listOf(jobResponseDto, jobResponseDto, jobResponseDto)

            `when`(jobRepository.findAll()).thenReturn(jobs)
            `when`(jobMapper.toResponseDto(job)).thenReturn(jobResponseDto)

            assertEquals(expectedResult, jobService.getAll())

            verify(jobRepository, only()).findAll()
            verify(jobMapper, times(expectedResult.size)).toResponseDto(job)
        }

        @Test
        fun `getAll should return empty list when no jobs`() {
            val jobs = listOf<Job>()
            val expectedResult = listOf<JobResponseDto>()

            `when`(jobRepository.findAll()).thenReturn(jobs)

            assertEquals(expectedResult, jobService.getAll())

            verify(jobRepository, only()).findAll()
            verifyNoInteractions(jobMapper)
        }
    }

    @Nested
    @DisplayName("Job save unit tests")
    inner class SaveJobTests {

        @Test
        fun `save job should throw EntityDuplicationException when name exists`() {
            `when`(jobRepository.existsByName(jobRequestDto.name)).thenReturn(true)

            assertThrows<EntityDuplicationException> { jobService.save(jobRequestDto) }

            verify(jobRepository, only()).existsByName(jobRequestDto.name)
            verifyNoMoreInteractions(jobRepository, jobMapper)
        }

        @Test
        fun `save job should returned saved job when valid request`() {
            `when`(jobRepository.existsByName(jobRequestDto.name)).thenReturn(false)
            `when`(jobMapper.toEntity(jobRequestDto)).thenReturn(job)
            `when`(jobRepository.save(job)).thenReturn(job)
            `when`(jobMapper.toResponseDto(job)).thenReturn(jobResponseDto)

            assertEquals(jobResponseDto, jobService.save(jobRequestDto))

            verify(jobRepository, times(1)).existsByName(jobRequestDto.name)
            verify(jobMapper, times(1)).toEntity(jobRequestDto)
            verify(jobRepository, times(1)).save(job)
            verify(jobMapper, times(1)).toResponseDto(job)
        }
    }

    @Nested
    @DisplayName("Job update unit tests")
    inner class UpdateJobTests {

        @Test
        fun `update job should return updated job when valid request`() {
            `when`(jobRepository.existsByName(jobRequestDto.name)).thenReturn(false)
            `when`(jobRepository.findById(jobId)).thenReturn(Optional.of(job))
            doNothing().`when`(jobMapper).updateEntityFromRequestDto(jobRequestDto, job)
            `when`(jobMapper.toResponseDto(job)).thenReturn(jobResponseDto)

            assertEquals(jobResponseDto, jobService.update(jobId, jobRequestDto))

            verify(jobRepository, times(1)).existsByName(jobRequestDto.name)
            verify(jobRepository, times(1)).findById(jobId)
            verify(jobMapper, times(1)).toResponseDto(job)
            verify(jobMapper, times(1)).updateEntityFromRequestDto(jobRequestDto, job)
            verifyNoMoreInteractions(jobRepository, jobMapper)
        }

        @Test
        fun `update job should throw EntityDuplicationException when name exists`() {
            `when`(jobRepository.existsByName(jobRequestDto.name)).thenReturn(true)

            assertThrows<EntityDuplicationException> { jobService.update(jobId, jobRequestDto) }

            verify(jobRepository, only()).existsByName(jobRequestDto.name)
            verifyNoMoreInteractions(jobRepository, jobMapper)
        }

        @Test
        fun `update job should throw EntityNotFoundException when job not found by id`() {
            `when`(jobRepository.existsByName(jobRequestDto.name)).thenReturn(false)
            `when`(jobRepository.findById(jobId)).thenReturn(Optional.empty())

            assertThrows<EntityNotFoundException> { jobService.update(jobId, jobRequestDto) }

            verify(jobRepository, times(1)).existsByName(jobRequestDto.name)
            verify(jobRepository, times(1)).findById(jobId)
            verifyNoMoreInteractions(jobRepository, jobMapper)
        }
    }

    @Nested
    @DisplayName("Job delete unit tests")
    inner class DeleteJobTests {

        @Test
        fun `deleteById should delete job when job exists`() {
            `when`(jobRepository.existsById(jobId)).thenReturn(true)
            doNothing().`when`(jobRepository).deleteById(jobId)

            jobService.deleteById(jobId)

            verify(jobRepository, times(1)).existsById(jobId)
            verify(jobRepository, times(1)).deleteById(jobId)
        }

        @Test
        fun `deleteById should throw EntityNotFoundException when job not found by id`() {
            `when`(jobRepository.existsById(jobId)).thenReturn(false)

            assertThrows<EntityNotFoundException> { jobService.deleteById(jobId) }

            verify(jobRepository, only()).existsById(jobId)
            verifyNoMoreInteractions(jobRepository)
        }
    }
}