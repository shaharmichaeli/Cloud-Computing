package exercise1;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED) // ERROR 500 CODE
public class UnauthorizeException extends RuntimeException {
	private static final long serialVersionUID = -4383687061470914751L;

	public UnauthorizeException() {
	}

	public UnauthorizeException(String message) {
		super(message);
	}

	public UnauthorizeException(Throwable cause) {
		super(cause);
	}

	public UnauthorizeException(String message, Throwable cause) {
		super(message, cause);
	}

}
