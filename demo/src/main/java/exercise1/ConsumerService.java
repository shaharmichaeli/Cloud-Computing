package exercise1;

public interface ConsumerService {
	public ConsumerBoundary create(ConsumerBoundary consumer);

	public ConsumerBoundary getByEmail(String email);

	public ConsumerBoundary login(String email, String password);

	public void update(ConsumerBoundary consumer);

	public void deleteAll();
}
