package demo

import org.springframework.stereotype.Component
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.regex.Pattern


@Component
class Validator {
    fun checkCustomerValidity(customer: CustomerBoundary?) {
        if (customer == null) {
            throw NotAcceptableException()
        }

        // Check all customer attributes
        checkEmailValidity(customer.email)
        checkNameValidity(customer.name)
        checkPasswordValidity(customer.password)
        checkBirthdateValidity(customer.birthdate)
        checkRolesValidity(customer.roles)
    }

    fun checkEmailValidity(email: String?) {
        checkStringValidity(email, EMAIL)
        val pattern = Pattern.compile(EMAIL_PATTERN)
        if (!pattern.matcher(email).matches()) {
            throw NotAcceptableException("Invalid email")
        }
    }

    fun checkNameValidity(name: Name?) {
        if (name == null) {
            throw EmptyFieldException("Name field is missing")
        }
        checkStringValidity(name.first, FIRST)
        checkStringValidity(name.last, LAST)
    }

    fun checkPasswordValidity(password: String?) {
        checkStringValidity(password, PASSWORD)

        // Check password contains at least 5 characters
        if (password!!.length < 5) {
            throw NotAcceptableException("Password must contains at least 5 characters")
        }

        // Check that password contains at least 1 digit
        var hasDigit = false
        val iterator: CharacterIterator = StringCharacterIterator(password)
        while (iterator.current() != CharacterIterator.DONE) {
            if (Character.isDigit(iterator.current())) {
                hasDigit = true
                break
            }
            iterator.next()
        }
        if (!hasDigit) {
            throw NotAcceptableException("Password must contains at least 1 digit")
        }
    }

    fun checkBirthdateValidity(birthdateStr: String?) {
        val formatter = DateTimeFormatter.ofPattern(BIRTHDATE_PATTERN)
        try {
            val birthdate = LocalDate.parse(birthdateStr, formatter)
            if (birthdate.isAfter(LocalDate.now().minusYears(MIN_AGE.toLong()))) {
                throw NotAcceptableException("Customers must be older than " + MIN_AGE)
            }
        } catch (e: DateTimeParseException) {
            throw NotAcceptableException("Invalid birthdate. Valid format: dd-MM-yyyy")
        }
    }

    fun checkRolesValidity(roles: Array<String>) {
        if (roles == null) {
            throw EmptyFieldException("Roles field is missing")
        }
        var counter = 1
        for (role in roles) {
            checkStringValidity(role, ROLE + " #" + counter)
            counter++
        }
    }

    fun checkFriendValidity(friend: FriendBoundary?) {
        if (friend == null) {
            throw NotAcceptableException()
        }

        // Check email
        checkEmailValidity(friend.email)
    }

    fun checkStringValidity(str: String?, field: String) {
        if (str == null || str.isEmpty()) {
            throw EmptyFieldException("$field cannot be empty string")
        }
    }

    companion object {
        private const val EMAIL_PATTERN = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"
        private const val BIRTHDATE_PATTERN = "dd-MM-yyyy"
        private const val EMAIL = "Email"
        private const val FIRST = "First name"
        private const val LAST = "Last name"
        private const val PASSWORD = "Password"
        private const val ROLE = "Role"
        private const val MIN_AGE = 16
    }
}
