package com.example.resmanback.service.impl;

import com.example.resmanback.model.DishIngredient;
import com.example.resmanback.repository.DishIngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishIngredientService {

    private final DishIngredientRepository dishIngredientRepository;

    public DishIngredientService(DishIngredientRepository dishIngredientRepository) {
        this.dishIngredientRepository = dishIngredientRepository;
    }

    public List<DishIngredient> getAllDishIngredients() {
        return dishIngredientRepository.findAll();
    }

    public List<DishIngredient> getIngredientsByDishId(Long dishId) {
        return dishIngredientRepository.findByDishId(dishId);
    }

    public DishIngredient addDishIngredient(DishIngredient dishIngredient) {
        return dishIngredientRepository.save(dishIngredient);
    }

    public void deleteDishIngredient(Long id) {
        dishIngredientRepository.deleteById(id);
    }
}
