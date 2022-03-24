package exercise1;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerBoundary {
	private String email;
	private String name;
	// Output boundary - Not display password.
	@JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String birthdate;
	private String[] roles;

	public CustomerBoundary() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

}
