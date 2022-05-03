package exercise3;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

@Document(collection = "orders")
public class OrderEntity {
	@Id
	private String id;
	private String userEmail;
	private Date createdTimeStamp;
	private Date fulfilledTimestamp;
	private List<OrderItemEntity> products;

	public OrderEntity() {
		super();
	}

	public OrderEntity(String id, String email, Date createdTimeStamp, Date fulfilledTimestamp,
			List<OrderItemEntity> products) {
		super();
		this.id = id;
		this.userEmail = email;
		this.createdTimeStamp = createdTimeStamp;
		this.fulfilledTimestamp = fulfilledTimestamp;
		this.products = products;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return userEmail;
	}

	public void setEmail(String email) {
		this.userEmail = email;
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
