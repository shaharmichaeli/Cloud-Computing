package exercise2;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductCatalogService {
	public Mono<ProductBoundary> create(ProductBoundary product);

	public Flux<ProductBoundary> getAllProducts(String filterType, String filterValue, String sortBy, String sortOrder,
			int size, int page, float minPrice, float maxPrice);

	public Mono<Void> deleteAll();

	public Mono<ProductBoundary> getById(String productId);

}
