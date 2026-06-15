package com.example.controller;

import com.example.dto.PageResponse;
import com.example.entity.Product;
import com.example.entity.TheLogConverter;
import com.example.service.ProductLogService;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by Wishwa Prabodha on 3/23/2018.
 */

@RestController
@RequestMapping("categories/{categoryId}/products")
public class ProductController {

    private static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    public ProductService productService;
    @Autowired
    private ProductLogService productLogService;

    /**
     * List products within a category with optional filtering, pagination and sorting.
     *
     * @param categoryId required – path variable
     * @param keyword    optional – case-insensitive partial match on productName
     * @param enabled    optional – filter by enabled/disabled status
     * @param minPrice   optional – minimum selling price (inclusive)
     * @param maxPrice   optional – maximum selling price (inclusive)
     * @param page       zero-based page index (default 0)
     * @param size       page size (default 20)
     * @param sort       sort expression, e.g. "productName,asc" or "productsellingPrice,desc"
     *                   (repeat the param for multi-field sort)
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public PageResponse<Product> getAllProducts(
            @PathVariable int categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort) {

        Pageable pageable = buildPageable(page, size, sort);

        return productService.findByCategory(categoryId, keyword, enabled, minPrice, maxPrice, pageable);
    }

    @RequestMapping("/{id}")
    public Optional<Product> searchProduct(@PathVariable int id) {
        return productService.find(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    public void addProduct(@RequestBody Product product) {
        productService.insert(product);
        productLogService.insert(TheLogConverter.productLogConverter(product));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public void updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        productLogService.insert(TheLogConverter.productLogConverter(product));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteProduct(@RequestBody Product product) {
        productService.deleteProduct(product);
        productLogService.insert(TheLogConverter.productLogConverter(product));
    }

    // ─── helpers ───────────────────────────────────────────────────────

    /**
     * Build a {@link Pageable} from request params.
     * Accepts sort in the form "field,direction" (e.g. "productName,asc").
     * Multiple sort params can be comma-separated: "productName,asc,productsellingPrice,desc".
     */
    private Pageable buildPageable(int page, int size, String sort) {
        if (sort == null || sort.isBlank()) {
            return PageRequest.of(page, size);
        }

        String[] parts = sort.split(",");
        Sort.Order[] orders = new Sort.Order[parts.length / 2];
        for (int i = 0; i + 1 < parts.length; i += 2) {
            String field = parts[i].trim();
            String direction = parts[i + 1].trim();
            orders[i / 2] = new Sort.Order(
                    "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC,
                    field
            );
        }
        return PageRequest.of(page, size, Sort.by(orders));
    }

}
