package com.example.resmanback.controller;

import com.example.resmanback.model.DishIngredient;
import com.example.resmanback.service.DishIngredientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dish-ingredients")
public class DishIngredientController {

    private final DishIngredientService dishIngredientService;

    public DishIngredientController(DishIngredientService dishIngredientService) {
        this.dishIngredientService = dishIngredientService;
    }

    @GetMapping
    public List<DishIngredient> getAllDishIngredients() {
        return dishIngredientService.getAllDishIngredients();
    }

    @GetMapping("/dish/{dishId}")
    public List<DishIngredient> getIngredientsByDishId(@PathVariable Long dishId) {
        return dishIngredientService.getIngredientsByDishId(dishId);
    }

    @PostMapping
    public DishIngredient addDishIngredient(@RequestBody DishIngredient dishIngredient) {
        return dishIngredientService.addDishIngredient(dishIngredient);
    }

    @DeleteMapping("/{id}")
    public void deleteDishIngredient(@PathVariable Long id) {
        dishIngredientService.deleteDishIngredient(id);
    }
}
