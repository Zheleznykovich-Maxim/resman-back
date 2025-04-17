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
        Map<String, Object> analytics = new HashMap<>();

        // Категории блюд: заказы и выручка
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

        // Ежедневная выручка
        Map<LocalDate, Double> dailyRevenue = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toLocalDate(),
                        Collectors.summingDouble(order -> order.getItems().stream()
                                .mapToDouble(item -> item.getDish().getPrice() * item.getQuantity())
                                .sum())
                ));

        // Популярные и прибыльные блюда
        Map<String, Long> dishPopularity = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getDish().getName(),
                        Collectors.counting()
                ));
        Map<String, Double> dishProfitability = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getDish().getName(),
                        Collectors.summingDouble(item -> item.getDish().getPrice() * item.getQuantity())
                ));

        // Топ-5 популярных блюд
        List<Map.Entry<String, Long>> topDishes = dishPopularity.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Топ-5 прибыльных блюд
        List<Map.Entry<String, Double>> topProfitableDishes = dishProfitability.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Использование ингредиентов
        Map<Object, Double> ingredientUsage = dishIngredientRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        dishIngredient -> dishIngredient.getIngredient().getName(),
                        Collectors.summingDouble(dishIngredient -> dishIngredient.getAmount())
                ));

        // Остатки ингредиентов
        Map<String, Double> ingredientStock = stockRepository.findAll().stream()
                .collect(Collectors.toMap(
                        stock -> stock.getIngredient().getName(),
                        Stock::getQuantity
                ));

        // Результаты
        analytics.put("categories", new ArrayList<>(categoryCounts.keySet()));
        analytics.put("orderCounts", new ArrayList<>(categoryCounts.values()));
        analytics.put("revenue", new ArrayList<>(categoryRevenue.values()));
        analytics.put("dates", new ArrayList<>(dailyRevenue.keySet()));
        analytics.put("dailyEarnings", new ArrayList<>(dailyRevenue.values()));
        analytics.put("topDishes", topDishes);
        analytics.put("topProfitableDishes", topProfitableDishes);
        analytics.put("ingredientUsage", ingredientUsage);
        analytics.put("ingredientStock", ingredientStock);

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
