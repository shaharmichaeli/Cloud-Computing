package demo

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(code = HttpStatus.NOT_FOUND)
class EmptyFieldException : RuntimeException {
    constructor() {}
    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    companion object {
        private const val serialVersionUID = -5673170043066568594L
    }
}
