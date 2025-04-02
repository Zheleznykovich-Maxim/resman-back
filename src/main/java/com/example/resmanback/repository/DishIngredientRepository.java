package com.example.resmanback.repository;

import com.example.resmanback.model.DishIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishIngredientRepository extends JpaRepository<DishIngredient, Long> {
    List<DishIngredient> findByDishId(Long dishId);
}
