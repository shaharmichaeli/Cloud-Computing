package demo

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(code = HttpStatus.NOT_FOUND)
class UserNotFoundExecption : RuntimeException {
    constructor() {}
    constructor(message: String?) : super(message) {}
    constructor(cause: Throwable?) : super(cause) {}
    constructor(message: String?, cause: Throwable?) : super(message, cause) {}

    companion object {
        private const val serialVersionUID = 4532735241282578711L
    }
}
