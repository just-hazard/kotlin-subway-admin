package nextstep.subway.exception

import org.springframework.http.HttpStatus

data class ApiError(val status: HttpStatus, val message: String) {
    constructor(message: String) : this(status = HttpStatus.INTERNAL_SERVER_ERROR, message = message)
    constructor(status: HttpStatus) : this(status = HttpStatus.INTERNAL_SERVER_ERROR, message = "errorMessage")
}
