package demo

import com.fasterxml.jackson.annotation.JsonProperty


class CustomerBoundary {
    var email: String? = null
    var name: Name? = null

    // Output boundary - Not display password.
    @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
    var password: String? = null
    var birthdate: String? = null
    lateinit var roles: Array<String>
}