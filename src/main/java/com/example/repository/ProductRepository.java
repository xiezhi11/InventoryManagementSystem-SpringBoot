package com.example.repository;

import com.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.category.categoryId = :categoryId" +
           " AND (:keyword IS NULL OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')))" +
           " AND (:enabled IS NULL OR p.enabled = :enabled)" +
           " AND (:minPrice IS NULL OR p.productsellingPrice >= :minPrice)" +
           " AND (:maxPrice IS NULL OR p.productsellingPrice <= :maxPrice)")
    Page<Product> findByCategoryWithFilters(
            @Param("categoryId") int categoryId,
            @Param("keyword") String keyword,
            @Param("enabled") Boolean enabled,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
