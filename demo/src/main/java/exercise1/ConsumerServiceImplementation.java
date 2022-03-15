package exercise1;

import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImplementation implements ConsumerService {

	public ConsumerServiceImplementation() {
		super();
	}

	@Override
	public ConsumerBoundary create(ConsumerBoundary consumer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConsumerBoundary getByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConsumerBoundary login(String email, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(ConsumerBoundary consumer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

}
