package com.example.resmanback.service;

import com.example.resmanback.model.Dish;
import com.example.resmanback.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElseThrow(() -> new RuntimeException("Dish not found"));
    }

    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish updateDish(Long id, Dish updatedDish) {
        Dish dish = getDishById(id);
        dish.setName(updatedDish.getName());
        dish.setPrice(updatedDish.getPrice());
        dish.setCategory(updatedDish.getCategory());
        return dishRepository.save(dish);
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
