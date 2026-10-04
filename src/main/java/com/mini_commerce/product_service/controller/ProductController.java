package com.mini_commerce.product_service.controller;

import com.mini_commerce.product_service.dto.StockUpdateRequest;
import com.mini_commerce.product_service.repository.Product;
import com.mini_commerce.product_service.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        productService.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable long id) {
        return productService.updateProduct(product, id);
    }

    @PatchMapping("/{id}/stock")
    public Product deductStock(
            @PathVariable Long id,
            @RequestBody StockUpdateRequest request) {

        return productService.deductStock(
                id,
                request.getQuantity()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable long id){
        productService.deleteProduct(id);
    }
}
