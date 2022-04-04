package exercise1.logic;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import exercise1.api.CustomerBoundary;
import exercise1.api.FriendBoundary;
import exercise1.api.Name;
import exercise1.logic.exceptions.EmptyFieldException;
import exercise1.logic.exceptions.NotAcceptableException;

@Component
public class Validator {

	private static final String EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
	private static final String BIRTHDATE_PATTERN = "dd-MM-yyyy";
	private static final String EMAIL = "Email";
	private static final String FIRST = "First name";
	private static final String LAST = "Last name";
	private static final String PASSWORD = "Password";
	private static final String ROLE = "Role";
	private static final int MIN_AGE = 16;

	public void checkCustomerValidity(CustomerBoundary customer) {
		if (customer == null) {
			throw new NotAcceptableException();
		}

		// Check all customer attributes
		checkEmailValidity(customer.getEmail());
		checkNameValidity(customer.getName());
		checkPasswordValidity(customer.getPassword());
		checkBirthdateValidity(customer.getBirthdate());
		checkRolesValidity(customer.getRoles());
	}

	public void checkEmailValidity(String email) {
		checkStringValidity(email, EMAIL);
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		if (!pattern.matcher(email).matches()) {
			throw new NotAcceptableException("Invalid email");
		}
	}

	public void checkNameValidity(Name name) {
		if (name == null) {
			throw new EmptyFieldException("Name field is missing");
		}

		checkStringValidity(name.getFirst(), FIRST);
		checkStringValidity(name.getLast(), LAST);
	}

	public void checkPasswordValidity(String password) {
		checkStringValidity(password, PASSWORD);

		// Check password contains at least 5 characters
		if (password.length() < 5) {
			throw new NotAcceptableException("Password must contains at least 5 characters");
		}

		// Check that password contains at least 1 digit
		boolean hasDigit = false;
		CharacterIterator iterator = new StringCharacterIterator(password);
		while (iterator.current() != CharacterIterator.DONE) {
			if (Character.isDigit(iterator.current())) {
				hasDigit = true;
				break;
			}
			iterator.next();
		}
		if (!hasDigit) {
			throw new NotAcceptableException("Password must contains at least 1 digit");
		}
	}

	public void checkBirthdateValidity(String birthdateStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BIRTHDATE_PATTERN);

		try {
			LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);
			if (birthdate.isAfter(LocalDate.now().minusYears(MIN_AGE))) {
				throw new NotAcceptableException("Customers must be older than " + MIN_AGE);
			}
		} catch (DateTimeParseException e) {
			throw new NotAcceptableException("Invalid birthdate. Valid format: dd-MM-yyyy");
		}
	}

	public void checkRolesValidity(String[] roles) {
		if (roles == null) {
			throw new EmptyFieldException("Roles field is missing");
		}

		int counter = 1;
		for (String role : roles) {
			checkStringValidity(role, ROLE + " #" + counter);
			counter++;
		}
	}

	public void checkFriendValidity(FriendBoundary friend) {
		if (friend == null) {
			throw new NotAcceptableException();
		}

		// Check email
		checkEmailValidity(friend.getEmail());
	}

	public void checkStringValidity(String str, String field) {
		if (str == null || str.isEmpty()) {
			throw new EmptyFieldException(field + " cannot be empty string");
		}
	}

}
