package exercise1;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class CustomerEntity {

	@Id
	private String email;
	private String emailDomain;
	private String name;
	private String password;
	private int birthdathDay;
	private int birthdateMonth;
	private int birthdateYear;

	private String[] roles;

	@ElementCollection
	@CollectionTable(name = "my_friends", joinColumns = @JoinColumn(name = "email"))
	@Column(name = "friends")
	private List<String> friendEmails;

	public CustomerEntity() {

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

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	public int getBirthdate_day() {
		return birthdathDay;
	}

	public void setBirthdate_day(int birthdate_day) {
		this.birthdathDay = birthdate_day;
	}

	public int getBirthdate_month() {
		return birthdateMonth;
	}

	public void setBirthdate_month(int birthdate_month) {
		this.birthdateMonth = birthdate_month;
	}

	public int getBirthdate_year() {
		return birthdateYear;
	}

	public void setBirthdate_year(int birthdate_year) {
		this.birthdateYear = birthdate_year;
	}

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	public List<String> getFriendEmails() {
		return friendEmails;
	}

	public void setFriendEmails(List<String> friendEmails) {
		this.friendEmails = friendEmails;
	}

}
