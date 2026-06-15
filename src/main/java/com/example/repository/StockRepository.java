package com.example.repository;

import com.example.entity.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends CrudRepository<Stock,Integer> {

    @Query("SELECT s FROM Stock s WHERE s.quantity <= s.safetyStockLevel "
            + "AND (:productId IS NULL OR s.product.productId = :productId) "
            + "AND (:supplierId IS NULL OR s.supplier.supplierId = :supplierId) "
            + "AND (:categoryId IS NULL OR s.category.categoryId = :categoryId)")
    List<Stock> findLowStock(@Param("productId") Integer productId,
                             @Param("supplierId") Integer supplierId,
                             @Param("categoryId") Integer categoryId);
}
