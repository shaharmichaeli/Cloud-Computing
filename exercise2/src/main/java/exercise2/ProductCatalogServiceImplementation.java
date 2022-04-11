package exercise2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductCatalogServiceImplementation implements ProductCatalogService {
	private ProductDAO productDAO;

	@Autowired
	public ProductCatalogServiceImplementation(ProductDAO productDAO) {
		super();
		this.productDAO = productDAO;
	}

	public ProductEntity boundaryToEntity(ProductBoundary productBoundary) {
		ProductEntity productEntity = new ProductEntity();
		productEntity.setId(productBoundary.getProductId());
		productEntity.setName(productBoundary.getName());
		productEntity.setPrice(productBoundary.getPrice());
		productEntity.setProductDetails(productBoundary.getProductDetails());
		productEntity.setCategory(productBoundary.getCategory());
		productEntity.setDescription(productBoundary.getDescription());

		return productEntity;

	}

	public ProductBoundary entityToBoundary(ProductEntity productEntity) {
		ProductBoundary productBoundary = new ProductBoundary();
		productBoundary.setProductId(productEntity.getId());
		productBoundary.setName(productEntity.getName());
		productBoundary.setPrice(productEntity.getPrice());
		productBoundary.setProductDetails(productEntity.getProductDetails());
		productBoundary.setCategory(productEntity.getCategory());
		productBoundary.setDescription(productEntity.getDescription());

		return productBoundary;

	}

	@Override
	public Mono<ProductBoundary> create(ProductBoundary product) {

		if (product.getProductId() == null)
			return Mono.error(() -> new RuntimeException("Product must have an ID."));
		
		Mono<ProductBoundary> productMono = Mono.just(product);

		return this.productDAO.findById(product.getProductId())
		.flatMap(result -> Mono.error(() -> new ProductAlreadyExistExecption(
				"Product Already Exist with the product ID : " + result.getId())))
		.switchIfEmpty(productMono)
		.map(prdBoundary -> {
			return boundaryToEntity((ProductBoundary) prdBoundary);
		})
		.flatMap(this.productDAO::save)
		.map(prdBoundary -> {
			return entityToBoundary((ProductEntity) prdBoundary);
		}).log();

		
//		return productMono
//				.map(prdBoundary -> {
//					if(prdBoundary.getProductId() == null) {
//						return Mono.error(() -> new RuntimeException("Product must have an ID."));
//					}else {
//						return prdBoundary;
//					}
//				}).
//				flatMap(prdBoundary -> {
//					return this.productDAO.findById(((ProductBoundary)prdBoundary).getProductId());
//				})
//				.flatMap(result -> Mono.error(() -> new ProductAlreadyExistExecption(
//						"Product Already Exist with the product ID : " + ((ProductEntity) result).getId())))
//				.switchIfEmpty(productMono)
//				.map(prdBoundary -> {
//					return boundaryToEntity((ProductBoundary) prdBoundary);
//				})
//				.flatMap(this.productDAO::save)
//				.map(prdBoundary -> {
//					return entityToBoundary((ProductEntity) prdBoundary);
//				}).log();
		

	}

	@Override
	public Mono<ProductBoundary> getById(String productId) {
		return this.productDAO.findById(productId).map(this::entityToBoundary).log();
	}

	@Override
	public Mono<Void> deleteAll() {
		return this.productDAO.deleteAll().log();
	}

	@Override
	public Flux<ProductBoundary> getAllProducts(String filterType, String filterValue, String sortBy, String sortOrder,
			int size, int page, float minPrice, float maxPrice) {

		if (!sortOrder.equals("ASC") && !sortOrder.equals("DESC")) {
			return Flux.error(() -> new RuntimeException("getAllProducts: Unacceptable Order."));
		}

		if (!sortBy.equals("productId") && !sortBy.equals("name") && !sortBy.equals("price")
				&& !sortBy.equals("description") && !sortBy.equals("productDetails") && !sortBy.equals("category")) {
			return Flux.error(() -> new RuntimeException("getAllProducts: Unacceptable Sort Attribute."));
		}

		if (sortBy.equals("productId")) {
			sortBy = "id";
		}

		Direction direction = sortOrder.equals("ASC") ? Direction.ASC : Direction.DESC;

		if (filterType != null && filterType.equals("byName")) {
			if (filterValue == null) {
				return Flux.error(() -> new RuntimeException("getAllProducts: criteriaValue is empty."));

			}
			return this.productDAO.findAllByName(filterValue, PageRequest.of(page, size, direction, sortBy))
					.map(this::entityToBoundary).log();

		} else if (filterType != null && filterType.equals("byCategoryName")) {
			if (filterValue == null) {
				return Flux.error(() -> new RuntimeException("getAllProducts: criteriaValue is empty."));
			}
			return this.productDAO.findAllByCategory(filterValue, PageRequest.of(page, size, direction, sortBy))
					.map(this::entityToBoundary).log();

		} else if (filterType != null && filterType.equals("byPrice")) {
			if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
				return Flux.error(() -> new RuntimeException("getAllProducts: criteriaValue is empty."));
			}
			return this.productDAO
					.findAllByPriceBetween(minPrice, maxPrice, PageRequest.of(page, size, direction, sortBy))
					.map(this::entityToBoundary).log();

		} else {
			return this.productDAO.findAllByIdNotNull(PageRequest.of(page, size, direction, sortBy))
					.map(this::entityToBoundary).log();
		}

	}
}
