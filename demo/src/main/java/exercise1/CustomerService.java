package exercise1;

import java.util.List;

public interface CustomerService {
	public CustomerBoundary create(CustomerBoundary consumer);

	public CustomerBoundary getByEmail(String email);

	public CustomerBoundary login(String email, String password);

	public void update(String email, CustomerBoundary consumer);

	public void deleteAll();

	public List<CustomerBoundary> getAllCustomers(int size, int page, String sortAttribute, String order,String criteriaType,String criteriaValue);
}
