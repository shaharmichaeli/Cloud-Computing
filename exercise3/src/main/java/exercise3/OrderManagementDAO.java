package exercise3;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderManagementDAO extends ReactiveMongoRepository<OrderEntity, String> {
	public Flux<OrderEntity> findAllByUserEmailAndFulfilledTimestampIsNull(String userEmail);
	public Flux<OrderEntity> findAllByUserEmail(String userEmail);
	public Mono<OrderEntity> findByUserEmailAndFulfilledTimestampIsNull(String email);

}
