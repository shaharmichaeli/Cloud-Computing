package demo

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR) // ERROR 500 CODE
class CustomerAlreadyExistExecption : RuntimeException {
    constructor() {}
    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    companion object {
        private const val serialVersionUID = -6115904545317055186L
    }
}
