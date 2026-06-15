package com.example.service;

import com.example.dto.PageResponse;
import com.example.entity.Product;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

/**
 * Created by Wishwa Prabodha on 3/23/2018.
 */


@Transactional
@Service
public class ProductService {

    @Autowired
    public ProductRepository productRepository;

    public void insert(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> find(int id) {
        return productRepository.findById(id);
    }

    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * Paginated + filtered product search within a category.
     *
     * @param categoryId required category filter
     * @param keyword    optional product name keyword (case-insensitive partial match)
     * @param enabled    optional enabled/disabled filter
     * @param minPrice   optional minimum selling price (inclusive)
     * @param maxPrice   optional maximum selling price (inclusive)
     * @param pageable   page / size / sort parameters
     * @return PageResponse containing the matched page of products
     */
    public PageResponse<Product> findByCategory(int categoryId,
                                                 String keyword,
                                                 Boolean enabled,
                                                 Double minPrice,
                                                 Double maxPrice,
                                                 Pageable pageable) {
        Page<Product> page = productRepository.findByCategoryWithFilters(
                categoryId, keyword, enabled, minPrice, maxPrice, pageable);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }


}
