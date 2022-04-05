package exercise2;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR) // ERROR 500 CODE

public class ProductAlreadyExistExecption extends RuntimeException {

	private static final long serialVersionUID = -1369696651987849315L;

	public ProductAlreadyExistExecption() {
	}

	public ProductAlreadyExistExecption(String message) {
		super(message);
	}

	public ProductAlreadyExistExecption(Throwable cause) {
		super(cause);
	}

	public ProductAlreadyExistExecption(String message, Throwable cause) {
		super(message, cause);
	}

}
