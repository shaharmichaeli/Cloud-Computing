package exercise3;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderItemEntity {
	private String productId;
	private int quantity;

	public OrderItemEntity() {
		super();
	}

	public OrderItemEntity(String productId, int quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
