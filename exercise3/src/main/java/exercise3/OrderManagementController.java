package exercise3;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

@Controller
public class OrderManagementController {
    private final Log logger;

	private OrderManagementService orderManagementService;
	@Autowired
	public OrderManagementController(OrderManagementService orderManagementService) {
		super();
        this.logger = LogFactory.getLog(OrderManagementController.class);
		this.orderManagementService = orderManagementService;
	}

	@PostConstruct
	public void init() {
		Hooks.onErrorDropped(e -> {
		});
	}

	//java -jar rsc-0.9.1.jar --debug --request --data "{\"orderId\":\"123\",\"email\":\"shahar@gmail.com\",\"products\":[{\"productId\":\"1\",\"quantity\":1}]}" --route order-req-resp tcp://localhost:7000	
	@MessageMapping("order-req-resp")
	public Mono<OrderBoundary> createRequestResponse(OrderBoundary orderBoundary) {
		return orderManagementService.create(orderBoundary);
	}

    // java -jar rsc-0.9.1.jar --debug --fnf --data "{\"orderId\":\"6270545230c6981f933eba2a\"}" --route fulfill-fire-and-forget tcp://localhost:7000
    @MessageMapping("fulfill-fire-and-forget")
    public Mono<Void> fullfill(OrderBoundary orderBoundary) {
		return orderManagementService.fullfil(orderBoundary);
    	
    }
    
    // java -jar rsc-0.9.1.jar --debug --stream --data "{\"userEmail\":\"shahar@gmail.com\"}" --route getOpenOrderItems-stream tcp://localhost:7000
    @MessageMapping("getOpenOrderItems-stream")
    public Flux<OrderItemBoundary> getOpenOrderItems(UserBoundary userBoundary) {
    	return orderManagementService.getOpenOrderItems(userBoundary);
    }
    
    // java -jar rsc-0.9.1.jar --debug --stream --data "{\"userEmail\":\"shahar@gmail.com\"}" --route getOrders-stream tcp://localhost:7000
    @MessageMapping("getOrders-stream")
    public Flux<OrderBoundary> getOrders(UserBoundary userBoundary) {
		return orderManagementService.getOrders(userBoundary);
    	
    }
    
    // java -jar rsc-0.9.1.jar --channel --data=- --route getItemsByOrder-channel tcp://localhost:7000
    @MessageMapping("getItemsByOrder-channel")
    public Flux<OrderItemBoundary> getItemsByOrder(Flux<OrderBoundary> orderBoundaryFlux) {
		return orderManagementService.getItemsByOrder(orderBoundaryFlux);
    }

    // java -jar rsc-0.9.1.jar --debug --fnf --route cleanup-fire-and-forget tcp://localhost:7000
	@MessageMapping("cleanup-fire-and-forget")
	public Mono<Void> deleteAllFNF() {
		return orderManagementService.deleteAll();
	}
}
