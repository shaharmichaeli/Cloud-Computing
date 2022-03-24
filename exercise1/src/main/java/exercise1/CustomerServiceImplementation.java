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
		String[] birthdate = cb.getBirthdate().split("-");
		entity.setBirthdate_day(Integer.parseInt(birthdate[0]));
		entity.setBirthdate_month(Integer.parseInt(birthdate[1]));
		entity.setBirthdate_year(Integer.parseInt(birthdate[2]));
		entity.setEmail(cb.getEmail());
		String[] emailParts = cb.getEmail().split("@");
		entity.setEmailDomain(emailParts[1]);
		entity.setName(cb.getName());
		entity.setPassword(cb.getPassword());
		entity.setRoles(cb.getRoles());
		return entity;
	}

	public CustomerBoundary entityToBoundary(CustomerEntity e) {
		CustomerBoundary cb = new CustomerBoundary();
		cb.setBirthdate(e.getBirthdate_day() + "-" + e.getBirthdate_month() + "-" + e.getBirthdate_year());
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
	public void update(String email, CustomerBoundary customer) {
		Optional<CustomerEntity> op = this.customerDAO.findById(email);
		if (op.isPresent()) {
			this.customerDAO.deleteById(email);
			CustomerEntity e = boundaryToEntity(customer);
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

		if (!order.equals("ASC") && !order.equals("DESC")) {
			throw new RuntimeException("getAllCustomers: Unacceptable Order");
		}

		Direction direction;
		if (order.equals("ASC")) {
			direction = Direction.ASC;

		} else {
			direction = Direction.DESC;
		}

		List<CustomerEntity> entities = null;
		if (criteriaType != null && criteriaType.equals("byBirthYear")) {
			entities = this.customerDAO.findAllByBirthdateYear(Integer.parseInt(criteriaValue),
					PageRequest.of(page, size, direction, sortAttribute));

		} else if (criteriaType != null && criteriaType.equals("byEmailDomain")) {
			entities = this.customerDAO.findAllByEmailDomain(criteriaValue,
					PageRequest.of(page, size, direction, sortAttribute));
		} else {
			Page<CustomerEntity> pageOfEntities = (Page<CustomerEntity>) this.customerDAO
					.findAll(PageRequest.of(page, size, direction, sortAttribute));
			entities = pageOfEntities.getContent();

		}

		List<CustomerBoundary> bounderies = new ArrayList<>();

		for (CustomerEntity entity : entities) {
			CustomerBoundary boundary = entityToBoundary(entity);
			bounderies.add(boundary);
		}
		return bounderies;

	}

	public void entityUpdate(String email, CustomerEntity customer) {
		this.customerDAO.deleteById(email);
		this.customerDAO.save(customer);

	}

	public void addFriend(String email, FriendBoundary friend) {
		Optional<CustomerEntity> main_op = this.customerDAO.findById(email);
		Optional<CustomerEntity> friend_op = this.customerDAO.findById(friend.getEmail());

		if (!main_op.isPresent()) {
			throw new UserNotFoundExecption("addFriend : User with " + email + " email not found.");
		}

		if (!main_op.isPresent()) {
			throw new UserNotFoundExecption("addFriend : User with " + friend.getEmail() + " email not found.");

		}

		CustomerEntity main_entity = main_op.get();
		List<String> friendEmails = main_entity.getFriendEmails();
		friendEmails.add(friend.getEmail());
		main_entity.setFriendEmails(friendEmails);

		CustomerEntity friend_entity = friend_op.get();
		friendEmails = friend_entity.getFriendEmails();
		friendEmails.add(main_entity.getEmail());
		friend_entity.setFriendEmails(friendEmails);
		entityUpdate(friend.getEmail(), friend_entity);

	}

	public List<CustomerBoundary> getAllFriends(int size, int page, String email) {
		Optional<CustomerEntity> op = this.customerDAO.findById(email);
		List<CustomerBoundary> bounderies = new ArrayList<>();

		if (op.isPresent()) {
			CustomerEntity customer = op.get();
			List<String> ids = customer.getFriendEmails();

			List<CustomerEntity> entities = this.customerDAO.findAllByEmailIn(ids, PageRequest.of(page, size));

			for (CustomerEntity entity : entities) {
				CustomerBoundary boundary = entityToBoundary(entity);
				bounderies.add(boundary);
			}

		}
		return bounderies;
	}

}
