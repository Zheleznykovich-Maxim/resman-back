package com.example.resmanback.service;

import com.example.resmanback.model.DishIngredient;
import com.example.resmanback.model.Order;
import com.example.resmanback.model.Stock;
import com.example.resmanback.repository.DishIngredientRepository;
import com.example.resmanback.repository.DishRepository;
import com.example.resmanback.repository.OrderRepository;
import com.example.resmanback.repository.StockRepository;
import org.springframework.stereotype.Service;
import com.example.resmanback.model.Dish;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final DishIngredientRepository dishIngredientRepository;
    private final StockRepository stockRepository;

    public AnalyticsService(DishRepository dishRepository,
                            DishIngredientRepository dishIngredientRepository,
                            StockRepository stockRepository,
                            OrderRepository orderRepository) {
        this.dishRepository = dishRepository;
        this.dishIngredientRepository = dishIngredientRepository;
        this.stockRepository = stockRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Создание графиков статистике на основе даннхы заказов блюд
     */
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

    /**
     * Рассчитывает доступное количество порций для каждого блюда
     */
    public Map<String, Integer> calculateAvailablePortions() {
        List<Dish> dishes = dishRepository.findAll();
        Map<String, Integer> availablePortions = new HashMap<>();

        for (Dish dish : dishes) {
            List<DishIngredient> dishIngredients = dishIngredientRepository.findByDishId(dish.getId());

            // Минимально возможное число порций
            int minPortions = Integer.MAX_VALUE;

            for (DishIngredient dishIngredient : dishIngredients) {
                Stock stock = stockRepository.findByIngredientId(dishIngredient.getIngredient().getId());

                if (stock == null || stock.getQuantity() <= 0) {
                    minPortions = 0; // Если ингредиента нет, блюдо приготовить невозможно
                    break;
                }

                // Сколько порций можно приготовить из текущего ингредиента
                int portions = (int) (stock.getQuantity() / dishIngredient.getAmount());

                // Берем минимум из всех рассчитанных значений
                minPortions = Math.min(minPortions, portions);
            }

            availablePortions.put(dish.getName(), minPortions);
        }

        return availablePortions;
    }
}
