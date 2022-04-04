package exercise2;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductDAO extends ReactiveMongoRepository<ProductEntity, String> {

	public Flux<ProductEntity> findAllByName(String name, Pageable pageable);

	public Flux<ProductEntity> findAllByCategory(String category, Pageable pageable);

	public Flux<ProductEntity> findAllByPriceBetween(int minPrice, int maxPrice, Pageable pageable);

	public Flux<ProductEntity> findAllByIdNotNull(Pageable pageable);

}
