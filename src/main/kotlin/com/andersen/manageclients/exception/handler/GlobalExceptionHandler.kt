package com.andersen.manageclients.exception.handler;

import com.andersen.manageclients.exception.EntityDuplicationException
import com.andersen.manageclients.model.ApplicationExceptionResponseDto
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException, response: HttpServletResponse): ApplicationExceptionResponseDto {
        response.status = HttpServletResponse.SC_BAD_REQUEST
        return ApplicationExceptionResponseDto(
            statusCode = HttpStatus.BAD_REQUEST.value(),
            message = "Validation Exception",
            exceptionMessage = e.message,
            timestamp = OffsetDateTime.now()
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleApiExceptions(e: EntityNotFoundException, response: HttpServletResponse): ApplicationExceptionResponseDto {
        response.status = HttpServletResponse.SC_NOT_FOUND
        return ApplicationExceptionResponseDto(
            statusCode = HttpStatus.NOT_FOUND.value(),
            message = "Not found",
            exceptionMessage = e.message ?: "",
            timestamp = OffsetDateTime.now()
        )
    }

    @ExceptionHandler(EntityDuplicationException::class)
    fun handleApiExceptions(e: EntityDuplicationException, response: HttpServletResponse): ApplicationExceptionResponseDto {
        response.status = HttpServletResponse.SC_CONFLICT
        return ApplicationExceptionResponseDto(
            statusCode = HttpStatus.CONFLICT.value(),
            message = "Duplicate unique field",
            exceptionMessage = e.message ?: "",
            timestamp = OffsetDateTime.now()
        )
    }


    @ExceptionHandler(Throwable::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleGenericApiException(e: Throwable): ApplicationExceptionResponseDto {
        return ApplicationExceptionResponseDto(
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "Unexpected exception",
            exceptionMessage = e.message ?: "",
            timestamp = OffsetDateTime.now()
        )
    }
}