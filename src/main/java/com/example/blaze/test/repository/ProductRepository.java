package com.example.blaze.test.repository;

import com.example.blaze.test.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
