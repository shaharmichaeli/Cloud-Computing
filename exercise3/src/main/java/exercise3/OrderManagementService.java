package exercise3;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderManagementService {
	public Mono<OrderBoundary> create(OrderBoundary order);
	public Mono<Void> fullfil(OrderBoundary order);
	public Mono<Void> deleteAll();
	public Flux<OrderItemBoundary> getOpenOrderItems(UserBoundary userBoundary);
	public Flux<OrderBoundary> getOrders(UserBoundary userBoundary);
	public Flux<OrderItemBoundary> getItemsByOrder(Flux<OrderBoundary> orderBoundaryFlux);

}
