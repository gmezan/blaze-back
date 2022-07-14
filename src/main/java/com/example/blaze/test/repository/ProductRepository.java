package com.example.blaze.test.repository;

import com.example.blaze.test.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{ id: { $regex: '^[0-9]' } }")
    Page<Product> findAll(Pageable pageable);
}
