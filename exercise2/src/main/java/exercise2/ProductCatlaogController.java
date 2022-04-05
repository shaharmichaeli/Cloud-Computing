package exercise2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ProductCatlaogController {
	private ProductCatalogService productCatalogService;

	@Autowired
	public ProductCatlaogController(ProductCatalogService productCatalogService) {
		super();
		this.productCatalogService = productCatalogService;
	}

	@RequestMapping(path = "/catalog", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ProductBoundary> create(@RequestBody ProductBoundary product) {
		return productCatalogService.create(product);
	}

	@RequestMapping(path = "/catalog/{productId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ProductBoundary> getById(@PathVariable("productId") String productId) {
		return productCatalogService.getById(productId);
	}

	@RequestMapping(path = "/catalog", method = RequestMethod.GET, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ProductBoundary> getAllProducts(@RequestParam(name = "filterType", required = false) String filterType,
			@RequestParam(name = "filterValue", required = false) String filterValue,
			@RequestParam(name = "sortBy", required = false, defaultValue = "productId") String sortBy,
			@RequestParam(name = "sortOrder", required = false, defaultValue = "ASC") String sortOrder,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "minPrice", required = false, defaultValue = "0") float minPrice,
			@RequestParam(name = "maxPrice", required = false, defaultValue = "0") float maxPrice) {
		return productCatalogService.getAllProducts(filterType, filterValue, sortBy, sortOrder, size, page, minPrice,
				maxPrice);
	}

	@RequestMapping(path = "/catalog", method = RequestMethod.DELETE)
	public Mono<Void> deleteAll() {
		return productCatalogService.deleteAll();
	}
}
