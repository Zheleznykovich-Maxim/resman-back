package com.example.resmanback.controller;

import com.example.resmanback.model.Dish;
import com.example.resmanback.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping
    public List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    @GetMapping("/{id}")
    public Dish getDishById(@PathVariable Long id) {
        return dishService.getDishById(id);
    }

    @PostMapping
    public Dish createDish(@RequestBody Dish dish) {
        return dishService.createDish(dish);
    }

    @PutMapping("/{id}")
    public Dish updateDish(@PathVariable Long id, @RequestBody Dish updatedDish) {
        return dishService.updateDish(id, updatedDish);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
