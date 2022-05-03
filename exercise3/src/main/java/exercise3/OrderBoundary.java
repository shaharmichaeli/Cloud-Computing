package exercise3;

import java.util.Date;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class OrderBoundary {
	private String orderId;
	private String email;
	private Date createdTimeStamp;
	private Date fulfilledTimestamp;
	private List<OrderItemEntity> products;

	public OrderBoundary() {
		super();
	}

	public OrderBoundary(String orderId, String email, Date createdTimeStamp, Date fulfilledTimestamp,
			List<OrderItemEntity> products) {
		super();
		this.orderId = orderId;
		this.email = email;
		this.createdTimeStamp = createdTimeStamp;
		this.fulfilledTimestamp = fulfilledTimestamp;
		this.products = products;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreatedTimeStamp() {
		return createdTimeStamp;
	}

	public void setCreatedTimeStamp(Date createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}

	public Date getFulfilledTimestamp() {
		return fulfilledTimestamp;
	}

	public void setFulfilledTimestamp(Date fulfilledTimestamp) {
		this.fulfilledTimestamp = fulfilledTimestamp;
	}

	public List<OrderItemEntity> getProducts() {
		return products;
	}

	public void setProducts(List<OrderItemEntity> products) {
		this.products = products;
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
