package com.example.orderservice.order.domain;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(String bookIsbn, int quantity) {
        return Mono.just(buildRejectOrder(bookIsbn, quantity)).flatMap(orderRepository::save);
    }

    private static Order buildRejectOrder(String bookIsbn, int quantity) {
        return Order.of(bookIsbn, null, null, quantity, OrderStatus.REJECTED);
    }
}
