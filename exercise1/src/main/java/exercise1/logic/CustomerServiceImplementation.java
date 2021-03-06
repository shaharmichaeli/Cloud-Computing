package exercise1.logic;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import exercise1.api.CustomerBoundary;
import exercise1.api.FriendBoundary;
import exercise1.api.Name;
import exercise1.data.CustomerDAO;
import exercise1.data.CustomerEntity;
import exercise1.logic.exceptions.CustomerAlreadyExistExecption;
import exercise1.logic.exceptions.UnauthorizeException;
import exercise1.logic.exceptions.UserNotFoundExecption;

@Service
public class CustomerServiceImplementation implements CustomerService {
	private CustomerDAO customerDAO;
	private Validator validator;

	@Autowired
	public CustomerServiceImplementation(CustomerDAO customerDAO, Validator validator) {
		this.customerDAO = customerDAO;
		this.validator = validator;
	}

	public CustomerEntity boundaryToEntity(CustomerBoundary cb) {
		CustomerEntity entity = new CustomerEntity();
		String[] birthdate = cb.getBirthdate().split("-");
		entity.setDay(Integer.parseInt(birthdate[0]));
		entity.setMonth(Integer.parseInt(birthdate[1]));
		entity.setYear(Integer.parseInt(birthdate[2]));
		entity.setEmail(cb.getEmail());
		String[] emailParts = cb.getEmail().split("@");
		entity.setEmailDomain(emailParts[1]);
		entity.setFirstName(cb.getName().getFirst());
		entity.setLastName(cb.getName().getLast());
		entity.setPassword(cb.getPassword());
		entity.setRoles(cb.getRoles());
		return entity;
	}

	public CustomerBoundary entityToBoundary(CustomerEntity e) {
		CustomerBoundary cb = new CustomerBoundary();
		cb.setBirthdate(e.getDay() + "-" + e.getMonth() + "-" + e.getYear());
		cb.setEmail(e.getEmail());
		cb.setName(new Name(e.getFirstName(), e.getLastName()));
		cb.setPassword(e.getPassword());
		cb.setRoles(e.getRoles());
		return cb;
	}

	@Override
	public CustomerBoundary create(CustomerBoundary cb) {
		this.validator.checkCustomerValidity(cb);
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
		this.validator.checkEmailValidity(email);
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
		this.validator.checkEmailValidity(email);
		Optional<CustomerEntity> op = this.customerDAO.findById(email);
		if (op.isPresent()) {
			CustomerEntity tempEntity = op.get();
			if (tempEntity.getPassword().equals(password)) {
				CustomerBoundary tempBoundary = entityToBoundary(tempEntity);
				return tempBoundary;
			} else {
				throw new UnauthorizeException("Login Method Failed.");

			}
		}

		throw new UnauthorizeException("Login Method Failed.");

	}

	@Override
	public void update(String email, CustomerBoundary customer) {
		Optional<CustomerEntity> op = this.customerDAO.findById(email);
		if (op.isPresent()) {
			CustomerEntity oldEntity = op.get();
			this.customerDAO.deleteById(email);

			CustomerEntity e = new CustomerEntity();
			e.setEmail(email); // Email can't be changed.

			if (customer.getBirthdate() != null) {
				this.validator.checkBirthdateValidity(customer.getBirthdate());
				String[] birthdate = customer.getBirthdate().split("-");
				e.setDay(Integer.parseInt(birthdate[0]));
				e.setMonth(Integer.parseInt(birthdate[1]));
				e.setYear(Integer.parseInt(birthdate[2]));
			} else {
				e.setDay(oldEntity.getDay());
				e.setMonth(oldEntity.getMonth());
				e.setYear(oldEntity.getYear());
			}

			if (customer.getName() != null) {
				this.validator.checkNameValidity(customer.getName());
				e.setFirstName(customer.getName().getFirst());
				e.setLastName(customer.getName().getLast());
			} else {
				e.setFirstName(oldEntity.getFirstName());
				e.setLastName(oldEntity.getLastName());
			}

			if (customer.getPassword() != null) {
				this.validator.checkPasswordValidity(customer.getPassword());
				e.setPassword(customer.getPassword());
			} else {
				e.setPassword(oldEntity.getPassword());
			}

			if (customer.getRoles() != null) {
				this.validator.checkRolesValidity(customer.getRoles());
				e.setRoles(customer.getRoles());
			} else {
				e.setRoles(oldEntity.getRoles());
			}

			this.customerDAO.save(e);
		} else {
			throw new UserNotFoundExecption("Update Method - User Not Found.");
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
			if (criteriaValue == null) {
				throw new RuntimeException("getAllCustomers: criteriaValue is empty.");
			}
			entities = this.customerDAO.findAllByYear(Integer.parseInt(criteriaValue),
//			entities = this.customerDAO.findAllByBirthdateYear(Integer.parseInt(criteriaValue),
					PageRequest.of(page, size, direction, sortAttribute));

		} else if (criteriaType != null && criteriaType.equals("byEmailDomain")) {
			if (criteriaValue == null) {
				throw new RuntimeException("getAllCustomers: criteriaValue is empty.");
			}
			entities = this.customerDAO.findAllByEmailDomain(criteriaValue,
					PageRequest.of(page, size, direction, sortAttribute));
		}else if (criteriaType != null && criteriaType.equals("byRole")) {
			if (criteriaValue == null) {
				throw new RuntimeException("getAllCustomers: criteriaValue is empty.");
			}
			return getAllByRole(size, page, criteriaValue);
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
		this.validator.checkFriendValidity(friend);
		Optional<CustomerEntity> main_op = this.customerDAO.findById(email);
		Optional<CustomerEntity> friend_op = this.customerDAO.findById(friend.getEmail());

		if (!main_op.isPresent()) {
			throw new UserNotFoundExecption("addFriend : User with " + email + " email not found.");
		}

		if (!friend_op.isPresent()) {
			throw new UserNotFoundExecption("addFriend : User with " + friend.getEmail() + " email not found.");
		}

		CustomerEntity main_entity = main_op.get();
		CustomerEntity friend_entity = friend_op.get();

		if (main_entity.getEmail().equals(friend_entity.getEmail())) {
			throw new RuntimeException("addFriend: Customer can not be a friend of his own.");
		}

		main_entity.addFriend(friend.getEmail());
		friend_entity.addFriend(main_entity.getEmail());
		entityUpdate(friend.getEmail(), friend_entity);
	}

	public List<CustomerBoundary> getAllFriends(int size, int page, String email) {
		this.validator.checkEmailValidity(email);
		Optional<CustomerEntity> op = this.customerDAO.findById(email);
		List<CustomerBoundary> bounderies = new ArrayList<>();

		if (op.isPresent()) {
			CustomerEntity customer = op.get();
			Set<String> friendEmails = customer.getFriendEmails();

			List<CustomerEntity> entities = this.customerDAO.findAllByEmailIn(friendEmails, PageRequest.of(page, size));

			for (CustomerEntity entity : entities) {
				CustomerBoundary boundary = entityToBoundary(entity);
				bounderies.add(boundary);
			}

		}
		return bounderies;
	}
	public List<CustomerBoundary> getAllByRole(int size, int page, String role) {
		this.validator.checkRolesValidity(new String[]{role});
//		List<CustomerEntity> entities = this.customerDAO.findAllBy(new String [] {role}, PageRequest.of(0, Integer.MAX_VALUE-1));
		List<CustomerEntity> entities = new ArrayList<>();
		Page<CustomerEntity> pageOfEntities = (Page<CustomerEntity>) this.customerDAO
				.findAll(PageRequest.of(0, Integer.MAX_VALUE-1));
						//, Direction.ASC, Properties.));
		entities = pageOfEntities.getContent();

		List<CustomerBoundary> bounderies = new ArrayList<>();
		List<CustomerBoundary> resultsList = new ArrayList<>();

		if (entities.size() > 0) {
//
			for (CustomerEntity entity : entities) {
				if(Arrays.asList(entity.getRoles()).contains(role)){
					CustomerBoundary boundary = entityToBoundary(entity);
					bounderies.add(boundary);
				}
			}
			// Handling pagination
			int currentIndex = page > 1 ? (page -1) * size : 0;
			for (int i = 0; i < size && i < bounderies.size() - currentIndex; i++) {
				resultsList.add(bounderies.get(currentIndex + i));
			}
		}
		return resultsList;
	}

	public List<CustomerBoundary> getAllSecondLevelFriends(int size, int page, String email) {
		List<CustomerBoundary> friendsBoundary = getAllFriends(size, page, email);
		//  Using a set to avoid duplicates
		Set<CustomerBoundary> secondLevelFriends = new HashSet<>();
		for(CustomerBoundary friend: friendsBoundary){
			secondLevelFriends.addAll(getAllFriends(size, page, friend.getEmail()));
		}
		friendsBoundary.addAll(secondLevelFriends);
		// Pagination handling
		List<CustomerBoundary> resultsList = new ArrayList<>();
		int currentIndex = page > 1 ? (page -1) * size : 0;
		for (int i = 0; i < size && i < friendsBoundary.size() - currentIndex; i++) {
			resultsList.add(friendsBoundary.get(currentIndex + i));
		}
		return resultsList;
	}

}
