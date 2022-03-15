package exercise1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
	private ConsumerService consumerService;

	@Autowired
	public ConsumerController(ConsumerService consumerService) {
		super();
		this.consumerService = consumerService;
	}

	@RequestMapping(path = "/costumers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ConsumerBoundary create(@RequestBody ConsumerBoundary consumer) {
		return consumerService.create(consumer);
	}

	@RequestMapping(path = "/costumers/byEmail/{email}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ConsumerBoundary getByEmail(@PathVariable String email) {
		return consumerService.getByEmail(email);
	}

	@RequestMapping(path = "/costumers/login/{email}?password={password}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ConsumerBoundary login(@PathVariable String email, @PathVariable String password) {
		return consumerService.login(email, password);
	}

	@RequestMapping(path = "/costumers/{email}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void update(@PathVariable String email, @RequestBody ConsumerBoundary consumer) {
		consumerService.update(consumer);
	}

//	@RequestMapping(path = "/customers/{email}/friends", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ConsumerBoundary create(@RequestParam(name = "email", required = true) String email) {
//		return consumerService.create(demo);
//	}
//
//	@RequestMapping(path = "/customers/{email}/friends?size={size}&page={page}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ConsumerBoundary create(@RequestParam(name = "email", required = true) String email) {
//		return consumerService.create(demo);
//	}

	@RequestMapping(path = "/customers", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void deleteAll() {
		consumerService.deleteAll();
	}

//	@RequestMapping(path = "/customers/{email}/friends?size={size}&page={page}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ConsumerBoundary create(@RequestBody ConsumerBoundary demo) {
//		return consumerService.create(demo);
//	}
//
//	@RequestMapping(path = "/customers/search?size={size}&page={page}&sortBy={sortAttribute}&sortOrder={order}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ConsumerBoundary create(@RequestBody ConsumerBoundary demo) {
//		return consumerService.create(demo);
//	}
//
//	@RequestMapping(path = "/customers/search?criteriaType=byEmailDomain&criteriaValue={value}&size={size}&page={page}&sortBy={sortAttribute}&sortOrder={order}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ConsumerBoundary create(@RequestBody ConsumerBoundary demo) {
//		return consumerService.create(demo);
//	}
//
//	@RequestMapping(path = "/customers/search?criteriaType=byBirthYear&criteriaValue={value}&size={size}&page={page}&sortBy={sortAttribute}&sortOrder={order}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ConsumerBoundary create(@RequestBody ConsumerBoundary demo) {
//		return consumerService.create(demo);
//	}
}
