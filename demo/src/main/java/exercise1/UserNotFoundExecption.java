package exercise1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundExecption extends RuntimeException {
	private static final long serialVersionUID = 4532735241282578711L;

	public UserNotFoundExecption() {
	}

	public UserNotFoundExecption(String message) {
		super(message);
	}

	public UserNotFoundExecption(Throwable cause) {
		super(cause);
	}

	public UserNotFoundExecption(String message, Throwable cause) {
		super(message, cause);
	}

}
