package exercise1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
	private CustomerService customerService;

	@Autowired
	public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}

	@RequestMapping(path = "/customers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerBoundary create(@RequestBody CustomerBoundary Customer) {
		return customerService.create(Customer);
	}

	@RequestMapping(path = "/customers/byEmail/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerBoundary getByEmail(@PathVariable String email) {
		return customerService.getByEmail(email);
	}

	@RequestMapping(path = "/customers/login/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomerBoundary login(@PathVariable String email,
			@RequestParam(name = "password", required = false) String password) {
		return customerService.login(email, password);
	}

	@RequestMapping(path = "/customers/{email}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update(@PathVariable String email, @RequestBody CustomerBoundary customer) {
		customerService.update(email, customer);
	}

	@RequestMapping(path = "/customers/{email}/friends", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void addFriend(@PathVariable String email, @RequestBody FriendBoundary friend) {
		customerService.addFriend(email, friend);
	}

	@RequestMapping(path = "/customers/{email}/friends", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CustomerBoundary> getFriends(@PathVariable String email,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return customerService.getAllFriends(size, page, email);
	}

	@RequestMapping(path = "/customers", method = RequestMethod.DELETE)
	public void deleteAll() {
		customerService.deleteAll();
	}

	// CriteriaValue can be ["byEmailDomain","byBirthYear"]
	@RequestMapping(path = "/customers/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CustomerBoundary> search(@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "sortBy", required = false, defaultValue = "email") String sortAttribute,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") String order,
			@RequestParam(name = "criteriaType", required = false) String criteriaType,
			@RequestParam(name = "criteriaValue", required = false) String criteriaValue) {
		return customerService.getAllCustomers(size, page, sortAttribute, order, criteriaType, criteriaValue);
	}

	@RequestMapping(path = "/customers/{email}/friends/secondLevel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CustomerBoundary> getSecondLevelFriends(@PathVariable String email,
											 @RequestParam(name = "size", required = false, defaultValue = "10") int size,
											 @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return customerService.getAllSecondLevelFriends(size, page, email);
	}
	///customers/{email}/friends/secondLevel?size={size}&page={page}
}
