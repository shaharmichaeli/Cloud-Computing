package exercise2;

import java.util.Map;

public class ProductBoundary {

	private String productId;
	private String name;
	private float price;
	private String description;
	public Map<String, Object> productDetails;
	private String category;

	public ProductBoundary() {

	}

	public ProductBoundary(String productId, String name, float price, String description,
			Map<String, Object> productDetails, String category) {
		super();
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.description = description;
		this.productDetails = productDetails;
		this.category = category;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	@Override
	public String toString() {
		return "ProductBoundary [id=" + productId + ", name=" + name + ", price=" + price + ", description="
				+ description + ", productDetails=" + productDetails + ", category=" + category + "]";
	}

}
