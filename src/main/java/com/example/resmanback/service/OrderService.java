package com.example.resmanback.service;

import com.example.resmanback.exception.OrderNotFoundException;
import com.example.resmanback.model.Dish;
import com.example.resmanback.model.Order;
import com.example.resmanback.model.OrderItem;
import com.example.resmanback.model.dto.OrderRequest;
import com.example.resmanback.repository.DishRepository;
import com.example.resmanback.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;

    public OrderService(OrderRepository orderRepository, DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order saveOrder(OrderRequest orderRequest) {
        List<OrderItem> items = orderRequest.getItems().stream().map(itemRequest -> {
            Dish dish = dishRepository.findById(itemRequest.getDishId())
                    .orElseThrow(() -> new RuntimeException("Dish not found with id: " + itemRequest.getDishId()));
            return new OrderItem(null, dish, itemRequest.getQuantity());
        }).collect(Collectors.toList());

        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setOrderDate(orderRequest.getOrderDate());
        order.setItems(items);

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order with ID " + id + " not found");
        }
        orderRepository.deleteById(id);
    }
}
