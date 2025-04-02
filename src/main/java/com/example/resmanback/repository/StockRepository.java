package com.example.resmanback.repository;

import com.example.resmanback.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByIngredientId(Long ingredientId);
}
