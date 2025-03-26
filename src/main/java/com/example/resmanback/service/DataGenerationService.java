package com.example.resmanback.service;

import com.example.resmanback.model.Dish;
import com.example.resmanback.model.Order;
import com.example.resmanback.model.OrderItem;
import com.example.resmanback.repository.DishRepository;
import com.example.resmanback.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataGenerationService {

    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;

    public DataGenerationService(DishRepository dishRepository, OrderRepository orderRepository) {
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
    }

    public void generateDishes(int count) {
        String[] categories = {"Main Course", "Dessert", "Beverage"};

        for (int i = 0; i < count; i++) {
            Dish dish = new Dish();
            dish.setName("Dish " + (i + 1));
            dish.setPrice(Math.round(Math.random() * 5000) / 100.0); // Цена в диапазоне от 0 до 50
            dish.setCategory(categories[i % categories.length]); // Ротация категорий
            dish.setImageUrl("/uploads/images/dish_" + (i + 1) + ".png");
            dishRepository.save(dish);
        }
    }

    public void generateOrders(int count) {
        for (int i = 0; i < count; i++) {
            Order order = Order.builder()
                    .orderDate(LocalDateTime.now().minusDays((long) (Math.random() * 30))) // Дата заказа в последние 30 дней
                    .customerName("Customer " + (i + 1))
                    .build();

            List<OrderItem> items = generateOrderItems();
            order.setItems(items);

            orderRepository.save(order);
        }
    }

    private List<OrderItem> generateOrderItems() {
        List<Dish> availableDishes = dishRepository.findAll();
        List<OrderItem> items = new ArrayList<>();

        if (availableDishes.isEmpty()) {
            throw new RuntimeException("No dishes available for order items.");
        }

        int itemCount = 1 + (int) (Math.random() * 5); // От 1 до 5 позиций в заказе
        for (int i = 0; i < itemCount; i++) {
            Dish dish = availableDishes.get((int) (Math.random() * availableDishes.size()));

            OrderItem item = new OrderItem();
            item.setDish(dish);
            item.setQuantity(1 + (int) (Math.random() * 3)); // Количество от 1 до 3
            items.add(item);
        }

        return items;
    }
}
