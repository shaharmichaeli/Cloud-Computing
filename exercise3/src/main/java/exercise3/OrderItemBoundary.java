package exercise3;

public class OrderItemBoundary {
	private String orderId;
	private String productId;
	private int quantity;
	
	
	public OrderItemBoundary() {
		super();
	}


	public OrderItemBoundary(String orderId, String productId, int quantity) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	
	
	
}
