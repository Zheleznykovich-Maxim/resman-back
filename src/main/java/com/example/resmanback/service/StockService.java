package com.example.resmanback.service;

import com.example.resmanback.model.Stock;
import com.example.resmanback.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public Stock getStockByIngredientId(Long ingredientId) {
        return stockRepository.findByIngredientId(ingredientId);
    }

    public Stock addStock(Stock stock) {
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
