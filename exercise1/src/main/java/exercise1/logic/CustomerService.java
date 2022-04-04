package exercise1.logic;

import java.util.List;

import exercise1.api.CustomerBoundary;
import exercise1.api.FriendBoundary;

public interface CustomerService {
	public CustomerBoundary create(CustomerBoundary consumer);

	public CustomerBoundary getByEmail(String email);

	public CustomerBoundary login(String email, String password);

	public void update(String email, CustomerBoundary consumer);

	public void deleteAll();

	public List<CustomerBoundary> getAllCustomers(int size, int page, String sortAttribute, String order,
			String criteriaType, String criteriaValue);

	public void addFriend(String email, FriendBoundary friend);

	public List<CustomerBoundary> getAllFriends(int size, int page, String email);

	public List<CustomerBoundary> getAllSecondLevelFriends(int size, int page, String email);

}
