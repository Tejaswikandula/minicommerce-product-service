package com.mini_commerce.product_service.service;

import com.mini_commerce.product_service.ProductNotFoundException;
import com.mini_commerce.product_service.repository.Product;
import com.mini_commerce.product_service.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }
//    public Product getProductById(Long id) {
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new IllegalStateException(
//                    "Product lookup interrupted", e
//            );
//        }
//
//        return productRepository.findById(id)
//                .orElseThrow();
//    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product updatedProduct, long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepository.delete(product);
    }

    @Transactional
    public Product deductStock(Long productId, int quantity) {

        // 1. Validate quantity
        if (quantity <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Quantity must be greater than zero"
            );
        }

        // 2. Retrieve the product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));

        // 3. Check available inventory
        if (product.getStock() < quantity) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Insufficient stock"
            );
        }

        int updatedRows = productRepository.deductStock(
                productId,
                quantity
        );

        if (updatedRows == 0) {

            if (!productRepository.existsById(productId)) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Insufficient stock"
            );
        }

        return productRepository.findById(productId)
                .orElseThrow();
    }
}
