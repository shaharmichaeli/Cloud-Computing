package exercise1.logic.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR) // ERROR 500 CODE
public class CustomerAlreadyExistExecption extends RuntimeException {
	private static final long serialVersionUID = -6115904545317055186L;

	public CustomerAlreadyExistExecption() {
	}

	public CustomerAlreadyExistExecption(String message) {
		super(message);
	}

	public CustomerAlreadyExistExecption(Throwable cause) {
		super(cause);
	}

	public CustomerAlreadyExistExecption(String message, Throwable cause) {
		super(message, cause);
	}

}
