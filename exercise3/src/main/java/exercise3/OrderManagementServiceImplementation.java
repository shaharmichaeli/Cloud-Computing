package exercise3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderManagementServiceImplementation implements OrderManagementService {
	private OrderManagementDAO orderMengementDAO;
	private final Log logger;

	@Autowired
	public OrderManagementServiceImplementation(OrderManagementDAO orderMengementDAO) {
		super();
		this.orderMengementDAO = orderMengementDAO;
		this.logger = LogFactory.getLog(OrderManagementServiceImplementation.class);
	}

	public OrderEntity boundaryToEntity(OrderBoundary orderBoundary) {
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(orderBoundary.getOrderId());
		orderEntity.setEmail(orderBoundary.getEmail());
		orderEntity.setCreatedTimeStamp(orderBoundary.getCreatedTimeStamp());
		orderEntity.setFulfilledTimestamp(orderBoundary.getFulfilledTimestamp());
		orderEntity.setProducts(orderBoundary.getProducts());

		return orderEntity;

	}

	public OrderBoundary entityToBoundary(OrderEntity orderEntity) {
		OrderBoundary orderBoundary = new OrderBoundary();
		orderBoundary.setOrderId(orderEntity.getId());
		orderBoundary.setEmail(orderEntity.getEmail());
		orderBoundary.setCreatedTimeStamp(orderEntity.getCreatedTimeStamp());
		orderBoundary.setFulfilledTimestamp(orderEntity.getFulfilledTimestamp());
		orderBoundary.setProducts(orderEntity.getProducts());

		return orderBoundary;

	}

	@Override
	public Mono<OrderBoundary> create(OrderBoundary orderBoundary) {
		Mono<OrderBoundary> boundary = Mono.just(orderBoundary);

		return this.orderMengementDAO.
				findByUserEmailAndFulfilledTimestampIsNull(orderBoundary.getEmail())
				.map(this::entityToBoundary)
				.switchIfEmpty(boundary)
				.zipWith(boundary)
				.map(tuple -> {
								if(tuple.getT1() == tuple.getT2()) {
									// orderBoundary take two places in tuple, indicate if the find didn't found an entity.
									logger.trace("Same Object.");
									tuple.getT1().setOrderId(null);
									tuple.getT1().setCreatedTimeStamp(new Date());
									return tuple.getT1();
								}
								logger.trace("Deleting1 : " + tuple.getT1());
								logger.trace("Deleting2 : " + tuple.getT2());
								
								// Update the product list.
								List<OrderItemEntity> new_products = new ArrayList<OrderItemEntity>();
								tuple.getT1().getProducts().addAll(tuple.getT2().getProducts());
								Map<String, List<OrderItemEntity>> products = tuple.getT1().getProducts().stream()
										.collect(Collectors.groupingBy(p -> p.getProductId()));
								for (String key : products.keySet()) {
									OrderItemEntity new_product = products.get(key).stream().reduce(new OrderItemEntity(), (total, e) -> {
										total.setQuantity(total.getQuantity() + e.getQuantity());
										return total;
									});
									new_product.setProductId(key);
									if(new_product.getQuantity() >= 0) 									
										new_products.add(new_product);
								}
								logger.trace("New Products : " + new_products);
								this.orderMengementDAO.deleteById(tuple.getT1().getOrderId());
								tuple.getT1().setProducts(new_products);
								return tuple.getT1();
							}
				)
				.map(this::boundaryToEntity)
				.flatMap(this.orderMengementDAO::save)
				.map(this::entityToBoundary)
				.log();
	}

	@Override
	public Mono<Void> deleteAll() {
		return this.orderMengementDAO.deleteAll().log();
	}

	@Override
	public Mono<Void> fullfil(OrderBoundary orderBoundary) {

		return Mono.just(orderBoundary).flatMap(boundary -> {
			return this.orderMengementDAO.findById(orderBoundary.getOrderId());
		}).flatMap(entity -> {
			if (entity.getFulfilledTimestamp() == null) {
				entity.setFulfilledTimestamp(new Date());
				this.orderMengementDAO.deleteById(entity.getId());
				return this.orderMengementDAO.save(entity);
			}
			return Mono.empty();
		}).then().log();
	}

	@Override
	public Flux<OrderItemBoundary> getOpenOrderItems(UserBoundary userBoundary) {
		return this.orderMengementDAO.findAllByUserEmailAndFulfilledTimestampIsNull(userBoundary.getUserEmail())
				.map(entity -> {
					return entity.getProducts().stream().map(product -> new OrderItemBoundary(entity.getId(),
							product.getProductId(), product.getQuantity())).toList();
				}).flatMap(Flux::fromIterable).log();
	}

	@Override
	public Flux<OrderBoundary> getOrders(UserBoundary userBoundary) {
		return this.orderMengementDAO.findAllByUserEmail(userBoundary.getUserEmail()).map(this::entityToBoundary)
				.map(orderBoundary -> {
					orderBoundary.setProducts(null);
					return orderBoundary;
				}).log();
	}

}
