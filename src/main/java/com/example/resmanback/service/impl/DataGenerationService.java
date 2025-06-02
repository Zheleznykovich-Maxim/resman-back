package com.example.resmanback.service.impl;

import com.example.resmanback.model.*;
import com.example.resmanback.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.round;

@Service
public class DataGenerationService {

    @Value("${app.upload.gen-images.dir}")
    private String imagePathPrefix;

    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;
    private final StockRepository stockRepository;
    private final DishIngredientRepository dishIngredientRepository;
    private final OrderRepository orderRepository;

    private final Random random = new Random();

    public DataGenerationService(DishRepository dishRepository,
                                 IngredientRepository ingredientRepository,
                                 StockRepository stockRepository,
                                 DishIngredientRepository dishIngredientRepository,
                                 OrderRepository orderRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
        this.stockRepository = stockRepository;
        this.dishIngredientRepository = dishIngredientRepository;
        this.orderRepository = orderRepository;
    }

    public int generateDishes(int count) {
        String[] categories = {"Main Course", "Dessert", "Beverage"};
        List<Ingredient> allIngredients = ingredientRepository.findAll();
        int availableImages = 10; // Количество доступных изображений

        for (int i = 0; i < count; i++) {
            Dish dish = new Dish();
            dish.setName("Dish " + (i + 1));
            dish.setPrice(round(10.0 + Math.random() * 100 * 100.0) / 100.0); // Цена в диапазоне от 0 до 50
            dish.setCategory(categories[i % categories.length]); // Ротация категорий
            // Выбор изображения
            int randomImageIndex = random.nextInt(10) + 1; // Число от 1 до 10
            String imageName = imagePathPrefix + "/gen-dish" + randomImageIndex + ".jpg";
            dish.setImageUrl(imageName);

            dishRepository.save(dish);

            // Генерация DishIngredient
            generateDishIngredients(dish, allIngredients);
        }
        return count;
    }


    public int generateIngredients(int count) {
        for (int i = 0; i < count; i++) {
            String name = "Ingredient " + (i + 1);

            // Создаем ингредиент
            Ingredient ingredient = new Ingredient();
            ingredient.setName(name);
            ingredient.setUnit("kg");
            ingredientRepository.save(ingredient);

            // Добавляем в склад
            Stock stock = new Stock();
            stock.setIngredient(ingredient);
            stock.setQuantity(random.nextInt(100) + 1); // Количество от 1 до 100
            stockRepository.save(stock);
        }
        return count;
    }

    public int generateOrders(int count) {
        for (int i = 0; i < count; i++) {
            Order order = Order.builder()
                    .orderDate(LocalDateTime.now().minusDays(random.nextInt(30))) // Дата заказа в последние 30 дней
                    .customerName("Customer " + (i + 1))
                    .build();

            List<OrderItem> items = generateOrderItems();
            order.setItems(items);

            orderRepository.save(order);
        }
        return count;
    }

    private List<OrderItem> generateOrderItems() {
        List<Dish> availableDishes = dishRepository.findAll();
        List<OrderItem> items = new ArrayList<>();

        if (availableDishes.isEmpty()) {
            throw new RuntimeException("No dishes available for order items.");
        }

        int itemCount = 1 + random.nextInt(5); // От 1 до 5 позиций в заказе
        for (int i = 0; i < itemCount; i++) {
            Dish dish = availableDishes.get(random.nextInt(availableDishes.size()));

            OrderItem item = new OrderItem();
            item.setDish(dish);
            item.setQuantity(1 + random.nextInt(3)); // Количество от 1 до 3
            items.add(item);
        }

        return items;
    }

    private void generateDishIngredients(Dish dish, List<Ingredient> allIngredients) {
        if (allIngredients.isEmpty()) {
            throw new RuntimeException("No ingredients available for DishIngredient generation.");
        }

        int ingredientCount = 1 + random.nextInt(3); // От 1 до 3 ингредиентов на блюдо
        for (int i = 0; i < ingredientCount; i++) {
            Ingredient ingredient = allIngredients.get(random.nextInt(allIngredients.size()));

            DishIngredient dishIngredient = new DishIngredient();
            dishIngredient.setDish(dish);
            dishIngredient.setIngredient(ingredient);
            dishIngredient.setAmount(round(1.0 + random.nextDouble() * 10.0 * 100.0) / 100.0); // Количество от 0.1 до 1.0 (в единицах)

            dishIngredientRepository.save(dishIngredient);
        }
    }
}
