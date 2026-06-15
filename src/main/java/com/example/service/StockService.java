package com.example.service;

import com.example.entity.Stock;
import com.example.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    public void insert(Stock stock) {
        validateStock(stock);
        stockRepository.save(stock);
    }

    public Optional<Stock> findById(int id) {
        return stockRepository.findById(id);
    }

    public Iterable<Stock> findAll() {
        return stockRepository.findAll();
    }

    public List<Stock> findLowStock(Integer productId, Integer supplierId, Integer categoryId) {
        return stockRepository.findLowStock(productId, supplierId, categoryId);
    }

    public void updateStock(Stock stock) {
        validateStock(stock);
        stockRepository.save(stock);
    }

    public void deleteStock(Stock stock) {
        stockRepository.delete(stock);
    }

    private void validateStock(Stock stock) {
        if (stock.getQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity must not be negative");
        }
        if (stock.getSafetyStockLevel() < 0) {
            throw new IllegalArgumentException("Stock safety stock level must not be negative");
        }
    }


}
