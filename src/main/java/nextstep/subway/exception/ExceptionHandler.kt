package nextstep.subway.exception

import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun constraintViolationException() : ResponseEntity<HttpStatus> {
        return ResponseEntity(HttpStatus.CONFLICT)
    }
}