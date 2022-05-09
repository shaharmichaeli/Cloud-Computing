package demo

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(code = HttpStatus.UNAUTHORIZED) // ERROR 500 CODE
class UnauthorizeException : RuntimeException {
    constructor() {}
    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    companion object {
        private const val serialVersionUID = -4383687061470914751L
    }
}
