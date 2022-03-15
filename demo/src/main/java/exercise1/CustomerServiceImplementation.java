package exercise1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImplementation implements CustomerService {
	private CustomerDAO customerDAO;

	@Autowired
	public CustomerServiceImplementation(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public CustomerEntity boundaryToEntity(CustomerBoundary cb) {
		CustomerEntity entity = new CustomerEntity();
		entity.setBirthdate(cb.getBirthdate());
		entity.setEmail(cb.getEmail());
		entity.setName(cb.getName());
		entity.setPassword(cb.getPassword());
		entity.setRoles(cb.getRoles());
		return entity;
	}

	public CustomerBoundary entityToBoundary(CustomerEntity e) {
		CustomerBoundary cb = new CustomerBoundary();
		cb.setBirthdate(e.getBirthdate());
		cb.setEmail(e.getEmail());
		cb.setName(e.getName());
		cb.setPassword(e.getPassword());
		cb.setRoles(e.getRoles());
		return cb;
	}

	@Override
	public CustomerBoundary create(CustomerBoundary cb) {
		Optional<CustomerEntity> op = this.customerDAO.findById(cb.getEmail());
		if (op.isPresent()) {
			throw new CustomerAlreadyExistExecption("Create Method - There is already user with this email.");
		}
		CustomerEntity e = boundaryToEntity(cb);
		this.customerDAO.save(e);
		return cb;
	}

	@Override
	public CustomerBoundary getByEmail(String email) {
		Optional<CustomerEntity> op = this.customerDAO.findById(email);
		if (op.isPresent()) {
			CustomerEntity tempEntity = op.get();
			CustomerBoundary tempBoundary = entityToBoundary(tempEntity);
			return tempBoundary;
		} else {
			throw new UserNotFoundExecption("getByEmail Method - User Not Found.");
		}
	}

	@Override
	public CustomerBoundary login(String email, String password) {
		Optional<CustomerEntity> op = this.customerDAO.findById(email);
		if (op.isPresent()) {
			CustomerEntity tempEntity = op.get();
			if (tempEntity.getPassword().equals(password)) {
				CustomerBoundary tempBoundary = entityToBoundary(tempEntity);
				return tempBoundary;
			} else {
				throw new UnauthorizeException("Login Method - Password Incorrect.");

			}
		}

		throw new UserNotFoundExecption("Login Method - User Not Found.");

	}

	@Override
	public void update(String email, CustomerBoundary consumer) {
		Optional<CustomerEntity> op = this.customerDAO.findById(email);
		if (op.isPresent()) {
			this.customerDAO.deleteById(email);
			CustomerEntity e = boundaryToEntity(consumer);
			this.customerDAO.save(e);
		} else {
			throw new UserNotFoundExecption("Login Method - User Not Found.");
		}

	}

	@Override
	public void deleteAll() {
		this.customerDAO.deleteAll();
	}

	@Override
	public List<CustomerBoundary> getAllCustomers(int size, int page, String sortAttribute, String order,
			String criteriaType, String criteriaValue) {

		// *** TODO - Add criteriaType by BirthYear, just to filter the entities like
		// the byEmailDomain entities..
		if (!order.equals("ASC") && !order.equals("DESC")) {
			throw new RuntimeException("getAllCustomers: Unacceptable Order");
		}

		Page<CustomerEntity> pageOfUsers;
		if (order.equals("ASC")) {
			pageOfUsers = this.customerDAO.findAll(PageRequest.of(page, size, Direction.ASC, sortAttribute));
		} else {
			pageOfUsers = this.customerDAO.findAll(PageRequest.of(page, size, Direction.DESC, sortAttribute));
		}

		List<CustomerEntity> entities = pageOfUsers.getContent();

		List<CustomerBoundary> bounderies = new ArrayList<>();

		for (CustomerEntity entity : entities) {
			if (criteriaType.equals("byEmailDomain")) {
				String[] email_parts = entity.getEmail().split("@");
				if (email_parts[email_parts.length - 1].equals(criteriaValue)) {
					CustomerBoundary boundary = entityToBoundary(entity);
					bounderies.add(boundary);
				}
			}
		}

		return bounderies;

	}

}
