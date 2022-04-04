package exercise1.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class CustomerEntity {

	@Id
	private String email;
	private String emailDomain;
	private String firstName;
	private String lastName;
	private String password;
	private int day;
	private int month;
	private int year;

	private String[] roles;

	@ElementCollection
	@CollectionTable(name = "my_friends", joinColumns = @JoinColumn(name = "email"))
	@Column(name = "friends")
	private Set<String> friendEmails;

	public CustomerEntity() {
		this.friendEmails = new HashSet<>();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	public Set<String> getFriendEmails() {
		return friendEmails;
	}

	public void setFriendEmails(Set<String> friendEmails) {
		this.friendEmails = friendEmails;
	}
	
	public void addFriend(String friendEmail) {
		this.friendEmails.add(friendEmail);
	}
	

}
