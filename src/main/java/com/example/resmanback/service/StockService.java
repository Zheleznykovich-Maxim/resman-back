package com.example.resmanback.service;

import com.example.resmanback.model.Ingredient;
import com.example.resmanback.model.Stock;
import com.example.resmanback.repository.IngredientRepository;
import com.example.resmanback.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final IngredientRepository ingredientRepository;

    public StockService(StockRepository stockRepository, IngredientRepository ingredientRepository) {
        this.stockRepository = stockRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public Stock getStockByIngredientId(Long ingredientId) {
        return stockRepository.findByIngredientId(ingredientId);
    }

    public Stock addStock(Stock stock) {
        Long ingredientId = stock.getIngredient().getId();

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        stock.setIngredient(ingredient);

        return stockRepository.save(stock);
    }

    public Stock updateStock(Long id, Stock updatedStock) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        stock.setQuantity(updatedStock.getQuantity());
        return stockRepository.save(stock);
    }

    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }
}
