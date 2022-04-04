package exercise2;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class ProductEntity {

	@Id
	private String id;
	private String name;
	private float price;
	private String description;
	public Map<String, Object> productDetails;
	private String category;

	public ProductEntity() {

	}

	public ProductEntity(String productId, String name, float price, String description,
			Map<String, Object> productDetails, String category) {
		super();
		this.id = productId;
		this.name = name;
		this.price = price;
		this.description = description;
		this.productDetails = productDetails;
		this.category = category;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(Map<String, Object> productDetails) {
		this.productDetails = productDetails;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
