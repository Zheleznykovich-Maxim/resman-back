package com.example.resmanback.service;

import com.example.resmanback.model.Order;
import com.example.resmanback.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final OrderRepository orderRepository;

    public AnalyticsService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Map<String, Object> generateAnalytics() {
        List<Order> orders = orderRepository.findAll();

        Map<String, Long> categoryCounts = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getDish().getCategory(),
                        Collectors.counting()
                ));

        Map<String, Double> categoryRevenue = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getDish().getCategory(),
                        Collectors.summingDouble(item -> item.getDish().getPrice() * item.getQuantity())
                ));

        Map<LocalDate, Double> dailyRevenue = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toLocalDate(),
                        Collectors.summingDouble(order -> order.getItems().stream()
                                .mapToDouble(item -> item.getDish().getPrice() * item.getQuantity())
                                .sum())
                ));

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("categories", new ArrayList<>(categoryCounts.keySet()));
        analytics.put("orderCounts", new ArrayList<>(categoryCounts.values()));
        analytics.put("revenue", new ArrayList<>(categoryRevenue.values()));

        // Подготовка данных для графика заработка по дням
        List<String> dates = dailyRevenue.keySet().stream()
                .map(LocalDate::toString)
                .collect(Collectors.toList());
        List<Double> dailyEarnings = new ArrayList<>(dailyRevenue.values());

        analytics.put("dates", dates);
        analytics.put("dailyEarnings", dailyEarnings);

        return analytics;
    }
}
