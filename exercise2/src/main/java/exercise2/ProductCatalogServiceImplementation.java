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
		// TODO add check if there are already product with the product id.
		return this.productDAO.save(this.boundaryToEntity(product)).map(this::entityToBoundary).log();
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
			throw new RuntimeException("getAllProducts: Unacceptable Order.");
		}

		if (!sortBy.equals("productId") && !sortBy.equals("name") && !sortBy.equals("price")
				&& !sortBy.equals("description") && !sortBy.equals("productDetails") && !sortBy.equals("category")) {
			throw new RuntimeException("getAllProducts: Unacceptable Sort Attribute - " + sortBy + ".");
		}

		if (sortBy.equals("productId")) {
			sortBy = "id";
		}

		Direction direction = sortOrder.equals("ASC") ? Direction.ASC : Direction.DESC;
		Flux<ProductBoundary> boundaries = null;

		if (filterType != null && filterType.equals("byName")) {
			if (filterValue == null) {
				throw new RuntimeException("getAllProducts: criteriaValue is empty.");
			}
			boundaries = this.productDAO.findAllByName(filterValue, PageRequest.of(page, size, direction, sortBy))
					.map(this::entityToBoundary).log();

		} else if (filterType != null && filterType.equals("byCategoryName")) {
			if (filterValue == null) {
				throw new RuntimeException("getAllProducts: criteriaValue is empty.");
			}
			boundaries = this.productDAO.findAllByCategory(filterValue, PageRequest.of(page, size, direction, sortBy))
					.map(this::entityToBoundary).log();

		} else if (filterType != null && filterType.equals("byPrice")) {
			if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
				throw new RuntimeException("getAllProducts: Max Price should be greater then Min Price.");
			}
			boundaries = this.productDAO
					.findAllByPriceBetween(minPrice, maxPrice, PageRequest.of(page, size, direction, sortBy))
					.map(this::entityToBoundary).log();

		} else {
			boundaries = this.productDAO.findAllByIdNotNull(PageRequest.of(page, size, direction, sortBy))
					.map(this::entityToBoundary).log();
		}
		return boundaries;

	}
}
